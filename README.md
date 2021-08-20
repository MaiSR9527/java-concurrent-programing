# java-concurrent-programing

## 一、Random随机数

每次都需要拿着老种子去更新新种子，内部使用原子类的更新，使用CAS操作保证原子性，但是在多线程的环境下，会有大量的线程在自旋，这回降低性能。所以ThreadLocalRandom应运而生。

```java
public int nextInt(int bound) {
        if (bound <= 0)
            throw new IllegalArgumentException(BadBound);
		// 根据老种子生成新种子
        int r = next(31);
    	// 根据新种子生成随机数
        int m = bound - 1;
        if ((bound & m) == 0)  // i.e., bound is a power of 2
            r = (int)((bound * (long)r) >> 31);
        else {
            for (int u = r;
                 u - (r = u % bound) + m < 0;
                 u = next(31))
                ;
        }
        return r;
    }


protected int next(int bits) {
        long oldseed, nextseed;
    	// 原子类，保证操作的原子性
        AtomicLong seed = this.seed;
        do {
            oldseed = seed.get();
            nextseed = (oldseed * multiplier + addend) & mask;
        } while (!seed.compareAndSet(oldseed, nextseed));  // 自旋
        return (int)(nextseed >>> (48 - bits));
    }
```

## 二、ThreadLocalRandom

Random多个线程是使用同一个种子产生争抢，为了保证原子性操作，引入原子类通过自旋产生新种子。ThreadLocalRandom跟ThreadLocal一样，ThreadLocalRandom每个线程会使用自己的种子。ThreadLocalRandom的nextInt方法实际上是获取当前线程的threadLocalRandomSeed变量，在根据新种子并使用具体的算法计算随机数。因为些变量都是线程级别的，每个线程都有，所以不需要使用原子性变量。

Thread.java

```java
	/** The current seed for a ThreadLocalRandom */
    @sun.misc.Contended("tlr")
    long threadLocalRandomSeed;

    /** Probe hash value; nonzero if threadLocalRandomSeed initialized */
    @sun.misc.Contended("tlr")
    int threadLocalRandomProbe;

    /** Secondary seed isolated from public ThreadLocalRandom sequence */
    @sun.misc.Contended("tlr")
    int threadLocalRandomSecondarySeed;
```

其中seeder和probeGenerator是原子变量，在初始化调用线程的种子和探针变量时会用到，每个线程只会使用一次。多线程调用current()获取ThreadLocalRandom实例时是同一个实例，因为那是static修饰的，种子是存放在自己的线程中，多线程之间相互隔离没有影响的。

