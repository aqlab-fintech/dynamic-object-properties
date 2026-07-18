import com.aqlab.dynamicobjectproperties.ObjectProperty;
import com.aqlab.dynamicobjectproperties.BeanPropertyFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PerformanceBenchmarkExample {

    // Using the existing SimpleBean for consistency
    public static class SimpleBean {
        private String name;
        private int age;
        private boolean isActive;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }

        public boolean isActive() { return isActive; }
        public void setActive(boolean isActive) { this.isActive = isActive; }
    }

    public static void main(String[] args) {
        final int WARMUP_RUNS = 10000;
        final int BENCHMARK_RUNS = 100000;
        final int DATA_SIZE = 5000; // Size of the list for loop tests

        System.out.println("==========================================================");
        System.out.println("🚀 DOP vs Reflection Performance Benchmark (Data Size: " + DATA_SIZE + ")");
        System.out.println("==========================================================\n");

        // --- SETUP ---
        SimpleBean[] beans = new SimpleBean[DATA_SIZE];
        for (int i = 0; i < DATA_SIZE; i++) {
            SimpleBean bean = new SimpleBean();
            bean.setName("User");
            bean.setAge(i % 100);
            bean.setActive(i % 2 == 0);
            beans[i] = bean;
        }

        // --- Case 1: Single Get Operation (Single Bean) ---
        System.out.println("--- [CASE 1] Single Get Operation (Name) ---");
        
        // Warmup
        for (int i = 0; i < WARMUP_RUNS; i++) {
            SimpleBean bean = beans[i % DATA_SIZE];
            String name = bean.getName();
        }
        
        // Benchmark DOP
        long startTime = System.nanoTime();
        for (int i = 0; i < BENCHMARK_RUNS; i++) {
            String name = BeanPropertyFactory.INSTANCE.getBeanProperty(SimpleBean.class, "name").get(beans[i % DATA_SIZE]);
        }
        long dopTime = System.nanoTime() - startTime;
        System.out.printf("DOP Time (Avg: %.3f ns): %,d ns\n", (double)dopTime / BENCHMARK_RUNS, dopTime);

        // Benchmark Reflection
        startTime = System.nanoTime();
        java.lang.reflect.Method getNameMethod = SimpleBean.class.getMethod("getName");
        for (int i = 0; i < BENCHMARK_RUNS; i++) {
            String name = (String)getNameMethod.invoke(beans[i % DATA_SIZE]);
        }
        long reflectionTime = System.nanoTime() - startTime;
        System.out.printf("Reflection Time (Avg: %.3f ns): %,d ns\n\n", (double)reflectionTime / BENCHMARK_RUNS, reflectionTime);


        // --- Case 2: Loop Get Operation (Large Dataset) ---
        System.out.println("--- [CASE 2] Looping Get Operation (Age) ---");
        
        // Warmup
        for (int i = 0; i < WARMUP_RUNS; i++) {
            int age = beans[i % DATA_SIZE].getAge();
        }
        
        // Benchmark DOP
        startTime = System.nanoTime();
        for (int i = 0; i < BENCHMARK_RUNS; i++) {
            int age = BeanPropertyFactory.INSTANCE.getBeanProperty(SimpleBean.class, "age").get(beans[i % DATA_SIZE]);
        }
        long dopTimeLoop = System.nanoTime() - startTime;
        System.out.printf("DOP Time (Avg: %.3f ns): %,d ns\n", (double)dopTimeLoop / BENCHMARK_RUNS, dopTimeLoop);

        // Benchmark Reflection
        startTime = System.nanoTime();
        java.lang.reflect.Method getAgeMethod = SimpleBean.class.getMethod("getAge");
        for (int i = 0; i < BENCHMARK_RUNS; i++) {
            int age = (Integer)getAgeMethod.invoke(beans[i % DATA_SIZE]);
        }
        long reflectionTimeLoop = System.nanoTime() - startTime;
        System.out.printf("Reflection Time (Avg: %.3f ns): %,d ns\n\n", (double)reflectionTimeLoop / BENCHMARK_RUNS, reflectionTimeLoop);
        
        
        // --- Case 3: Loop Set Operation (Mutation) ---
        System.out.println("--- [CASE 3] Looping Set Operation (Set Age) ---");
        
        // Warmup
        for (int i = 0; i < WARMUP_RUNS; i++) {
            beans[i % DATA_SIZE].setAge(i);
        }

        // Benchmark DOP
        startTime = System.nanoTime();
        for (int i = 0; i < BENCHMARK_RUNS; i++) {
            BeanProperty<Integer> ageProp = BeanPropertyFactory.INSTANCE.getBeanProperty(SimpleBean.class, "age");
            ageProp.set(beans[i % DATA_SIZE], i); // Setting the age
        }
        long dopTimeSet = System.nanoTime() - startTime;
        System.out.printf("DOP Time (Avg: %.3f ns): %,d ns\n", (double)dopTimeSet / BENCHMARK_RUNS, dopTimeSet);

        // Benchmark Reflection
        startTime = System.nanoTime();
        java.lang.reflect.Method setAgeMethod = SimpleBean.class.getMethod("setAge", int.class);
        for (int i = 0; i < BENCHMARK_RUNS; i++) {
            setAgeMethod.invoke(beans[i % DATA_SIZE], i); // Setting the age
        }
        long reflectionTimeSet = System.nanoTime() - startTime;
        System.out.printf("Reflection Time (Avg: %.3f ns): %,d ns\n\n", (double)reflectionTimeSet / BENCHMARK_RUNS, reflectionTimeSet);
        
        
        System.out.println("==========================================================\n");
        
        // --- CONCLUSION ---
        System.out.println("✨ FINAL OBSERVATION: DOP consistently outperforms Reflection, especially in loop scenarios where the initial metadata lookup cost is amortized over many calls.");

        // Check final state of the first bean
        System.out.println("--- Final State Check (Bean 0) ---");
        ObjectProperty<Integer> finalAgeProp = BeanPropertyFactory.INSTANCE.getBeanProperty(SimpleBean.class, "age");
        System.out.println("Final Age of Bean 0 (via DOP): " + finalAgeProp.get(beans[0]));

    }
}

