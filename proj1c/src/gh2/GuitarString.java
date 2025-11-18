package gh2;
import deque.Deque61B;
import deque.LinkedListDeque61B;
// TODO: maybe more imports

//Note: This file will not compile until you complete the Deque61B implementations
/**
 * GuitarString 模拟一根吉他弦的物理振动行为。
 * 使用 Karplus-Strong 算法生成逼真的音频样本。
 */
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    // TODO: uncomment the following line once you're ready to start this portion
    // private Deque61B<Double> buffer;
    /** 用于存储音频样本的环形缓冲区 */
    private Deque61B<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    /** 构造函数：根据频率初始化缓冲区 */
    public GuitarString(double frequency) {
        // TODO: Initialize the buffer with capacity = SR / frequency. You'll need to
        //       cast the result of this division operation into an int. For
        //       better accuracy, use the Math.round() function before casting.
        //       Your should initially fill your buffer with zeros.
        // 计算缓冲区容量：采样率除以频率，表示一个周期的样本数
        int capacity = (int) Math.round(SR / frequency);

        // 使用 LinkedListDeque61B 实现缓冲区
        buffer = new LinkedListDeque61B<>();

        // 初始化缓冲区为静止状态（全部填充为 0.0）
        for (int i = 0; i < capacity; i++) {
            buffer.addLast(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    /** 拨弦：用随机噪声替换缓冲区内容，模拟拨动弦的瞬间扰动 */
    public void pluck() {
        // TODO: Dequeue everything in buffer, and replace with random numbers
        //       between -0.5 and 0.5. You can get such a number by using:
        //       double r = Math.random() - 0.5;
        //
        //       Make sure that your random numbers are different from each
        //       other. This does not mean that you need to check that the numbers
        //       are different from each other. It means you should repeatedly call
        //       Math.random() - 0.5 to generate new random numbers for each array index.
        int size = buffer.size();

        // 清空原有缓冲区
        for (int i = 0; i < size; i++) {
            buffer.removeFirst();
        }

        // 填充新的随机值 [-0.5, 0.5]，模拟白噪声
        for (int i = 0; i < size; i++) {
            double r = Math.random() - 0.5;
            buffer.addLast(r);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    /** 执行一次 Karplus-Strong 算法迭代，更新缓冲区内容 */
    public void tic() {
        // TODO: Dequeue the front sample and enqueue a new sample that is
        //       the average of the two multiplied by the DECAY factor.
        //       **Do not call StdAudio.play().**
        // 取出前两个样本
        double first = buffer.removeFirst();
        double second = buffer.get(0);

        // 计算新样本：两个样本平均值乘以衰减因子
        double newSample = DECAY * 0.5 * (first + second);

        // 将新样本加入缓冲区末尾
        buffer.addLast(newSample);
    }

    /* Return the double at the front of the buffer. */
    /** 返回当前缓冲区前端的样本值，用于播放 */
    public double sample() {
        // TODO: Return the correct thing.
        return buffer.get(0);
    }
}
    // TODO: Remove all comments that say TODO when you're done.
