## Java编程方法论-响应式 之 Reactor篇

Spring官方以及龙之春力荐

01 Reactor开篇:https://www.bilibili.com/video/av35326911/?p=1

02 subscribe方法解读及原子类AtomicXxxFieldUpdater的技法解读及应用:https://www.bilibili.com/video/av35326911/?p=2

03 Flux.create解读及AtomicXxxFieldUpdater部分细节讲解修正及背压策略的具体实现细节解读:https://www.bilibili.com/video/av35326911/?p=3

04 Flux.Create回顾 BaseSubscriber与几个Flux源的快速包装方法源码细节解读:https://www.bilibili.com/video/av35326911/?p=4

05 结合Spring Cloud Function来讲函数式的诸多好处:https://www.bilibili.com/video/av35326911/?p=5

06 通过函数式来进行方法级别的类接口设计:https://www.bilibili.com/video/av35326911/?p=6

07 Flux.generate方法解读及使用其一个方法来对数据库事务实现进行拓展:https://www.bilibili.com/video/av35326911/?p=7

08 针对上一节末尾的口误的修正:https://www.bilibili.com/video/av35326911/?p=8

09 Reactor中无界队列的源码解读 :https://www.bilibili.com/video/av35326911/?p=9

10 Flux.generate中程序健壮性所涉及的error日志的触发演示及原理解读:https://www.bilibili.com/video/av35326911/?p=10

11 通过Reactor中Flux.generate的相关实现来fix Rxjava中generate源码中的bug(基于最新的2.2.3的发行版):https://www.bilibili.com/video/av35326911/?p=11

12 QueueSubscription蛇形走位的解读以及publishOn的详解1: https://www.bilibili.com/video/av35326911/?p=12

13 publishOn的详解2:https://www.bilibili.com/video/av35326911/?p=13

14 publishOn的详解3:https://www.bilibili.com/video/av35326911/?p=14

15 publishOn的详解4:https://www.bilibili.com/video/av35326911/?p=15

16 通过一个redis与数据库双写的场景来讲述Reactor中我们可以借鉴与对业务改造的思路1:https://www.bilibili.com/video/av35326911/?p=16

17 通过一个redis与数据库双写的场景来讲述Reactor中我们可以借鉴与对业务改造的思路2:https://www.bilibili.com/video/av35326911/?p=17

18 通过一个redis与数据库双写的场景来讲述Reactor中我们可以借鉴与对业务改造的思路3:https://www.bilibili.com/video/av35326911/?p=18

19 将常见的监听器改造成响应式结构:https://www.bilibili.com/video/av35326911/?p=19

20 Flux.push特殊使用场景及细节探索:https://www.bilibili.com/video/av35326911/?p=20

21 Flux.handle 解读:https://www.bilibili.com/video/av35326911/?p=21

22 Schedulers.elastic深入理解1:https://www.bilibili.com/video/av35326911/?p=22

23 Schedulers.elastic深入理解2:https://www.bilibili.com/video/av35326911/?p=23

24 Schedulers.elastic深入理解3:https://www.bilibili.com/video/av35326911/?p=24

25 Reactor中ElasticScheduler与Rxjava2的IoScheduler实现的优劣对比:https://www.bilibili.com/video/av35326911/?p=25

26 Flux.subscribeOn 深入解读1:https://www.bilibili.com/video/av35326911/?p=26

27 Flux.subscribeOn 深入解读2:https://www.bilibili.com/video/av35326911/?p=27

28 subscribeOn与publishOn默认使用上小bug解读:https://www.bilibili.com/video/av35326911/?p=28

29 Flux.parallel源码解读1:https://www.bilibili.com/video/av35326911/?p=29

30 Flux.parallel源码解读2:https://www.bilibili.com/video/av35326911/?p=30

31 ParallelFlux.runOn源码解读:https://www.bilibili.com/video/av35326911/?p=31

32 Flux.filter操作源码解读:https://www.bilibili.com/video/av35326911/?p=32

33 transform与compose 操作解读:https://www.bilibili.com/video/av35326911/?p=33

34 buﬀer 操作源码解读:https://www.bilibili.com/video/av35326911/?p=34

35 window操作源码解读:https://www.bilibili.com/video/av35326911/?p=35

36 Flux.groupBy 操作源码深入解读:https://www.bilibili.com/video/av35326911/?p=36

37 Flux.merge源码解读1:https://www.bilibili.com/video/av35326911/?p=37

38 Flux.merge源码解读2:https://www.bilibili.com/video/av35326911/?p=38

39 Flux.mergeSequential操作源码解读:https://www.bilibili.com/video/av35326911/?p=39

40 Flux.ﬂatMap源码深入解读:https://www.bilibili.com/video/av35326911/?p=40

41 Flux.concatMap源码深入解读1:https://www.bilibili.com/video/av35326911/?p=41

42 Flux.concatMap源码深入解读2:https://www.bilibili.com/video/av35326911/?p=42

43 Flux.combineLatest 操作源码深入解读:https://www.bilibili.com/video/av35326911/?p=43

44 Flux.publish及 Flux.replay 操作源码深入解读1:https://www.bilibili.com/video/av35326911/?p=44

45 Flux.publish及 Flux.replay 操作源码深入解读2:https://www.bilibili.com/video/av35326911/?p=45

46 Flux.publish及 Flux.replay 操作源码深入解读3:https://www.bilibili.com/video/av35326911/?p=46

47 Flux.publish及 Flux.replay 操作源码深入解读4:https://www.bilibili.com/video/av35326911/?p=47

48 UnicastProcessor详解:https://www.bilibili.com/video/av35326911/?p=48

49 DirectProcessor详解:https://www.bilibili.com/video/av35326911/?p=49

50 EmitterProcessor详解:https://www.bilibili.com/video/av35326911/?p=50

51 ReplayProcessor详解:https://www.bilibili.com/video/av35326911/?p=51

52 TopicProcessor初探:https://www.bilibili.com/video/av35326911/?p=52

53 TopicProcessor及reactor中借鉴disruptor匹配实现 1:https://www.bilibili.com/video/av35326911/?p=53

54 TopicProcessor及reactor中借鉴disruptor匹配实现 2 之WaitStrategy解读:https://www.bilibili.com/video/av35326911/?p=54

55 TopicProcessor及reactor中借鉴disruptor匹配实现 3 之subscribe方法的深度解读上:https://www.bilibili.com/video/av35326911/?p=55

56 TopicProcessor及reactor中借鉴disruptor匹配实现 4 之subscribe方法的深度解读下:https://www.bilibili.com/video/av35326911/?p=56

57 WorkQueueProcessor解读:https://www.bilibili.com/video/av35326911/?p=57

58 WorkQueueProcessor解读细节补充:https://www.bilibili.com/video/av35326911/?p=58

59 Hot 还是 Cold :https://www.bilibili.com/video/av35326911/?p=59

60 Hot 还是 Cold 总结补充:https://www.bilibili.com/video/av35326911/?p=60

61 Reactor特供的Context:https://www.bilibili.com/video/av35326911/?p=61

62 Reactor中的测试源码解读与实战 1:https://www.bilibili.com/video/av35326911/?p=62

63 Reactor中的测试源码解读与实战 2 操作时间测试:https://www.bilibili.com/video/av35326911/?p=63

64 Reactor中的测试源码解读与实战 3 使用 StepVerifier 进行后置校验:https://www.bilibili.com/video/av35326911/?p=64

65 Reactor中的测试源码解读与实战 4 关于Context的测试 上:https://www.bilibili.com/video/av35326911/?p=65

66 Reactor中的测试源码解读与实战 4 关于Context的测试 下:https://www.bilibili.com/video/av35326911/?p=66

67 Reactor中的测试源码解读与实战 5 使用TestPublisher对自定义中间操作测试:https://www.bilibili.com/video/av35326911/?p=67

68 Reactor中的测试源码解读与实战 5 使用TestPublisher对自定义中间操作测试 补充1:https://www.bilibili.com/video/av35326911/?p=68

69 Reactor中的测试源码解读与实战 5 使用TestPublisher对自定义中间操作测试 补充2:https://www.bilibili.com/video/av35326911/?p=69

70 Reactor中的测试 6 使用PublisherProbe检查执行路径:https://www.bilibili.com/video/av35326911/?p=70

71 Reactor中的调试 之 启用调试模式:https://www.bilibili.com/video/av35326911/?p=71

72 Reactor中 在调试模式下读取堆栈跟踪信息 及 通过checkpoint()方式进行调试 与 记录订阅关系下操作流程的日志:https://www.bilibili.com/video/av35326911/?p=72