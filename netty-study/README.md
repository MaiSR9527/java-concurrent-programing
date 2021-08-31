---
title: Netty源码剖析：Netty中服务端Channel的初始化过程
date: 2021-08-30 22:44:35
categories: java
tags: Netty
---

# 一、服务端socket初始化

## 1.1 创建服务端Channel

Netty在创建Channel的时候是把Jdk的Channel再次封装成自己的Channel。那么开始探索Netty是怎么创建出Jdk的Channel的。那么就从一段常用的代码开始：

```java
EventLoopGroup bossGroup = new NioEventLoopGroup(1);
EventLoopGroup workerGroup = new NioEventLoopGroup();

try {
    ServerBootstrap b = new ServerBootstrap();
    b.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childAttr(AttributeKey.newInstance("childAttr"), "childAttrValue")
            .handler(new ServerHandler())
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    // ch.pipeline().addLast(new ServerHandler());
                    //..

                }
            });

    ChannelFuture f = b.bind(8888).sync();

    f.channel().closeFuture().sync();
} finally {
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
}
```

Netty会封装成自己的channel
* 入口方法：bing(int port)

* initAndRegister()：初始化并注册

* channel = channelFactory.newChannel()：调用Jdk反射创建服务端Channel。上述的`.channel(NioServerSocketChannel.class)`创建了一个`ReflectiveChannelFactory`通过传入的class调用反射创建一个Channel，这个Channel就是`NioServerSocketChannel`。

**NioServerSocketChannel的构造函数：**

```java
public NioServerSocketChannel() {
    this(newSocket(DEFAULT_SELECTOR_PROVIDER));
}
```

* newSocket()：返回Jdk的ServerSocketChannel。

```java
public NioServerSocketChannel(ServerSocketChannel channel) {
    super(null, channel, SelectionKey.OP_ACCEPT);
    config = new NioServerSocketChannelConfig(this, javaChannel().socket());
}
```

* NioServerSocketChannelConfig()：对反射创建出来的Jdk的Channel配置tcp参数。在上面的super一直追踪到父类。

```java
protected AbstractNioChannel(Channel parent, SelectableChannel ch, int readInterestOp) {
    super(parent);
    this.ch = ch;
    this.readInterestOp = readInterestOp;
    try {
        // 非阻塞模式
        ch.configureBlocking(false);
    } catch (IOException e) {
        try {
            ch.close();
        } catch (IOException e2) {
            if (logger.isWarnEnabled()) {
                logger.warn(
                        "Failed to close a partially initialized socket.", e2);
            }
        }

        throw new ChannelException("Failed to enter non-blocking mode.", e);
    }
}
```

* 调用父类的AbstractNioChannel()  ===> configureBlocking(false)：非阻塞模式。
* super(parent)调用父类AbstractChannel()：创建id，unsafe，pipeline。Netty中Channel的三个属性

```java
protected AbstractChannel(Channel parent) {
    this.parent = parent;
    id = newId();
    unsafe = newUnsafe();
    pipeline = newChannelPipeline();
}
```

## 1.2 初始化服务端channel

newChannel()：创建出服务端的，就要对其进行初始化了。

```java
final ChannelFuture initAndRegister() {
    Channel channel = null;
    try {
        // 创建Channel
        channel = channelFactory.newChannel();
        // 初始化Channel
        init(channel);
    } catch (Throwable t) {
        if (channel != null) {
            // channel can be null if newChannel crashed (eg SocketException("too many open files"))
            channel.unsafe().closeForcibly();
        }
        // as the Channel is not registered yet we need to force the usage of the GlobalEventExecutor
        return new DefaultChannelPromise(channel, GlobalEventExecutor.INSTANCE).setFailure(t);
    }
	// 注册selector
    ChannelFuture regFuture = config().group().register(channel);
    if (regFuture.cause() != null) {
        if (channel.isRegistered()) {
            channel.close();
        } else {
            channel.unsafe().closeForcibly();
        }
    }

    // If we are here and the promise is not failed, it's one of the following cases:
    // 1) If we attempted registration from the event loop, the registration has been completed at this point.
    //    i.e. It's safe to attempt bind() or connect() now because the channel has been registered.
    // 2) If we attempted registration from the other thread, the registration request has been successfully
    //    added to the event loop's task queue for later execution.
    //    i.e. It's safe to attempt bind() or connect() now:
    //         because bind() or connect() will be executed *after* the scheduled registration task is executed
    //         because register(), bind(), and connect() are all bound to the same thread.

    return regFuture;
}
```

* 初始化入口：init(channel)

* set ChannelOptions，ChannelAttrs 设置连接的属性
* set ChildOptions，ChildAttrs 设置连接的属性
* config handler 配置服务端pipeline，在用户代码中配置的handler ===> `.handler(new ServerHandler())`
* add ServerBootstrapAcceptor 添加连接器

```java
void init(Channel channel) throws Exception {
    // 设置ChannelOptions
    final Map<ChannelOption<?>, Object> options = options0();
    synchronized (options) {
        channel.config().setOptions(options);
    }
	// 设置ChannelAttrs 
    final Map<AttributeKey<?>, Object> attrs = attrs0();
    synchronized (attrs) {
        for (Entry<AttributeKey<?>, Object> e: attrs.entrySet()) {
            @SuppressWarnings("unchecked")
            AttributeKey<Object> key = (AttributeKey<Object>) e.getKey();
            channel.attr(key).set(e.getValue());
        }
    }
	// 获取服务端Channel的pipeline。服务端Channel创建时，会创建出一个pipeline
    ChannelPipeline p = channel.pipeline();

    final EventLoopGroup currentChildGroup = childGroup;
    final ChannelHandler currentChildHandler = childHandler;
    final Entry<ChannelOption<?>, Object>[] currentChildOptions;
    final Entry<AttributeKey<?>, Object>[] currentChildAttrs;
    // 保存用户设置的ChildOptions
    synchronized (childOptions) {
        currentChildOptions = childOptions.entrySet().toArray(newOptionArray(childOptions.size()));
    }
    // 保存用户时设置的ChildAttrs
    synchronized (childAttrs) {
        currentChildAttrs = childAttrs.entrySet().toArray(newAttrArray(childAttrs.size()));
    }
	// 
    p.addLast(new ChannelInitializer<Channel>() {
        @Override
        public void initChannel(Channel ch) throws Exception {
            final ChannelPipeline pipeline = ch.pipeline();
            // 把用户自定义配置的handler添加到pipeline的处理链中。这里的handler就是在用户代码配置的
            ChannelHandler handler = config.handler();
            if (handler != null) {
                pipeline.addLast(handler);
            }
			
            // We add this handler via the EventLoop as the user may have used a ChannelInitializer as handler.
            // In this case the initChannel(...) method will only be called after this method returns. Because
            // of this we need to ensure we add our handler in a delayed fashion so all the users handler are
            // placed in front of the ServerBootstrapAcceptor.
            ch.eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    // 添加连接器，一个特殊的Handler。同时传入childHandler、childOptions、ChildAttrs
                    // 用户配置的childHandler就是在此时配置进pipeline。所以每来一个新连接，就会触发childHandler
                    pipeline.addLast(new ServerBootstrapAcceptor(
                            currentChildGroup, currentChildHandler, currentChildOptions, currentChildAttrs));
                }
            });
        }
    });
}
```

总的来说，初始化Channel很简单，就是把用户配置的Handler、Options和Attrs整合设置进pipeline。

## 1.3 注册selector

上面中对于initAndRegister() 方法只说完了init初始化阶段，register阶段就是注册selector ===> `ChannelFuture regFuture = config().group().register(channel);最终调用的是AbstractChannel的register方法。

步骤：

* 入口：AbstractChannel.register()
* 绑定线程：AbstractChannel.this.eventLoop = eventLoop
* 实际执行注册的方法：register0()
  * 调用Jdk底层进行selector注册：doRegister()
  * invokeHandlerAddedIfNeeded()
  * 传播事件：fireChannelRegistered()

```java
@Override
public final void register(EventLoop eventLoop, final ChannelPromise promise) {
    // 省略一些if判断代码
    
	// 绑定线程
    AbstractChannel.this.eventLoop = eventLoop;

    if (eventLoop.inEventLoop()) {
        // 实际注册
        register0(promise);
    } else {
        try {
            eventLoop.execute(new Runnable() {
                @Override
                public void run() {
                    // 实际注册
                    register0(promise);
                }
            });
        } catch (Throwable t) {
            // 省略一些异常处理
        }
    }
}


private void register0(ChannelPromise promise) {
    try {
        // check if the channel is still open as it could be closed in the mean time when the register
        // call was outside of the eventLoop
        if (!promise.setUncancellable() || !ensureOpen(promise)) {
            return;
        }
        boolean firstRegistration = neverRegistered;
        // 调用Jdk底层注册selector
        doRegister();
        neverRegistered = false;
        registered = true;

        // Ensure we call handlerAdded(...) before we actually notify the promise. This is needed as the
        // user may already fire events through the pipeline in the ChannelFutureListener.
        // 确保执行ChannelHandler中的handlerAdded()方法
        pipeline.invokeHandlerAddedIfNeeded();

        safeSetSuccess(promise);
        // 确保执行ChannelHandler中的handlerAdded()方法
        pipeline.fireChannelRegistered();
        // Only fire a channelActive if the channel has never been registered. This prevents firing
        // multiple channel actives if the channel is deregistered and re-registered.
        // 此时Channel还没绑定，所以此时为false
        if (isActive()) {
            if (firstRegistration) {
                pipeline.fireChannelActive();
            } else if (config().isAutoRead()) {
                // This channel was registered before and autoRead() is set. This means we need to begin read
                // again so that we process inbound data.
                //
                // See https://github.com/netty/netty/issues/4805
                beginRead();
            }
        }
    } catch (Throwable t) {
        // Close the channel directly to avoid FD leak.
        closeForcibly();
        closeFuture.setClosed();
        safeSetFailure(promise, t);
    }
}
```

当我们实现了自定义的ChannelHandler时，并重写了如：`handlerAdded(ChannelHandlerContext ctx)`、`channelRegistered(ChannelHandlerContext ctx)`和`channelActive(ChannelHandlerContext ctx)`这三个方法时，就会去回调这三个方法。

> pipeline.invokeHandlerAddedIfNeeded();     =====>  handlerAdded(ChannelHandlerContext ctx)
>
> pipeline.fireChannelRegistered();                   =====>  channelRegistered(ChannelHandlerContext ctx)
>
> pipeline.fireChannelActive();                            并不会触发 channelActive(ChannelHandlerContext ctx)  因为isActive()返回的是false。该回调操作的触发在下面的端口绑定中就会出现

doRedister()方法的实现，由于我们使用的Nio，所以进入AbstractNioChannel查看：

```java
@Override
protected void doRegister() throws Exception {
    boolean selected = false;
    for (;;) {
        try {
            // 最终调用Jdk底层的selector注册。javaChannel()是在前面提到的，Netty的Channel是在Jdk的创建出来的Channel进一步封装的。
            selectionKey = javaChannel().register(eventLoop().selector, 0, this);
            return;
        } catch (CancelledKeyException e) {
            if (!selected) {
                // Force the Selector to select now as the "canceled" SelectionKey may still be
                // cached and not removed because no Select.select(..) operation was called yet.
                eventLoop().selectNow();
                selected = true;
            } else {
                // We forced a select operation on the selector before but the SelectionKey is still cached
                // for whatever reason. JDK bug ?
                throw e;
            }
        }
    }
}
```

## 1.4 端口绑定

步骤：

* 入口：AbstractUnsafe.bind()
  * doBing()
    * 调用Jdk底层的端口绑定：javaChannel().bing()
  * 传播事件：pipeline.fireChannelActive()
    * 把之前绑定的事件重新绑定成accept事件：HeadContext.readIfIsAutoRead()

AbstractChannel：

```java
@Override
public final void bind(final SocketAddress localAddress, final ChannelPromise promise) {
    assertEventLoop();

    if (!promise.setUncancellable() || !ensureOpen(promise)) {
        return;
    }

    // See: https://github.com/netty/netty/issues/576
    if (Boolean.TRUE.equals(config().getOption(ChannelOption.SO_BROADCAST)) &&
        localAddress instanceof InetSocketAddress &&
        !((InetSocketAddress) localAddress).getAddress().isAnyLocalAddress() &&
        !PlatformDependent.isWindows() && !PlatformDependent.isRoot()) {
        // Warn a user about the fact that a non-root user can't receive a
        // broadcast packet on *nix if the socket is bound on non-wildcard address.
        logger.warn(
                "A non-root user can't receive a broadcast packet if the socket " +
                "is not bound to a wildcard address; binding to a non-wildcard " +
                "address (" + localAddress + ") anyway as requested.");
    }
	// 是否成功绑定
    boolean wasActive = isActive();
    try {
        // 进行端口绑定，NioServerSocketChannel.doBind(...)
        doBind(localAddress);
    } catch (Throwable t) {
        safeSetFailure(promise, t);
        closeIfClosed();
        return;
    }
	
    if (!wasActive && isActive()) {
        invokeLater(new Runnable() {
            @Override
            public void run() {
                // 传播active事件
                pipeline.fireChannelActive();
            }
        });
    }

    safeSetSuccess(promise);
}
```

`isActive()`方法中的代码可见，在完成端口绑定之后，才会触发active事件，这时候才能去传播：`pipeline.fireChannelActive()`。

```
@Override
public boolean isActive() {
    return javaChannel().socket().isBound();
}
```

使用的Nio所以doBind中调用NioServerSocketChannel.doBind(...)

```java
@Override
protected void doBind(SocketAddress localAddress) throws Exception {
    if (PlatformDependent.javaVersion() >= 7) {
        javaChannel().bind(localAddress, config.getBacklog());
    } else {
        javaChannel().socket().bind(localAddress, config.getBacklog());
    }
}
```

`pipeline.fireChannelActive()`对于Netty中时间传播，这里简单的讲一下，后面有机会再详述吧！

DefaultChannelPipeline：

```java
@Override
public void channelActive(ChannelHandlerContext ctx) throws Exception {
    // 传播active事件
    ctx.fireChannelActive();
	// 触发channel的read事件
    readIfIsAutoRead();
}
```

最终read事件是调用到了AbstractNioChannel.doBeginRead()

```java
@Override
protected void doBeginRead() throws Exception {
    // Channel.read() or ChannelHandlerContext.read() was called
    // 在上面的doRegister()方法中：selectionKey = javaChannel().register(eventLoop().selector, 0, this);
    // 此时interestOps=0
    final SelectionKey selectionKey = this.selectionKey;
    if (!selectionKey.isValid()) {
        return;
    }

    readPending = true;

    final int interestOps = selectionKey.interestOps();
    if ((interestOps & readInterestOp) == 0) {  // 0&0 = 0
        // 所以selectionKey的ops是0 而0对应的操作就是 OP_READ 服务端读取连接
        selectionKey.interestOps(interestOps | readInterestOp);
    }
}
```

# 二、总结

newChannel(创建Channel)   ===>  init()(初始化Channel，添加连接处理器)   ===>  register()(调用底层Jdk的方法注册selector)   ===>  diBind()(调用底层Jdk的方法进行端口绑定，传播active事件，触发channel的read事件)

  
