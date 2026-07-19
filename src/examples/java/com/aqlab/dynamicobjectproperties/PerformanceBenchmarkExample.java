package com.aqlab.dynamicobjectproperties;

import com.aqlab.dynamicobjectproperties.property.BeanProperty;
import com.aqlab.dynamicobjectproperties.property.BeanPropertyFactory;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

// -- TOP (Benchmark Entry Point) --
public class PerformanceBenchmarkExample {

    private static final int[] WARM_ITERATIONS = {1, 10, 100, 1000, 10000, 100000};
    private static final String CSV_FILE = "benchmark_results.csv";
    private static final String MD_FILE = "benchmark_results.md";

    // -- CSV row accumulator --
    static class Row {
        final String test;
        final int iterations;
        final long totalNs;
        final double avgNs;
        final String label;

        Row(String test, int iterations, long totalNs, String label) {
            this.test = test;
            this.iterations = iterations;
            this.totalNs = totalNs;
            this.avgNs = (double) totalNs / iterations;
            this.label = label;
        }
    }
    static final List<Row> rows = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        System.out.println("==========================================================");
        System.out.println("✨ DOP vs Reflection vs Direct Performance Benchmark");
        System.out.println("==========================================================\n");

        // -- SETUP --
        CityBean leaf = new CityBean();
        leaf.setCity("Shanghai");

        ProfileBean topBean = new ProfileBean();
        topBean.setName("Alex");
        topBean.setAge(30);
        topBean.setDetails(leaf);

        // -- Phase 1: Cold Start (first call, no warmup) --
        System.out.println("-- [PHASE 1] Cold Start (first call, JIT not primed) --");
        runColdStartTest(topBean, leaf);

        System.out.println("\n==========================================================\n");

        // -- Phase 2: Warmup & Throughput --
        System.out.println("-- [PHASE 2] Warm Iterations (pre-compiled / JIT hot) --");
        for (int n : WARM_ITERATIONS) {
            System.out.printf("-- [N=%,d] --%n", n);
            runWarmTest(topBean, leaf, n);
            System.out.println();
        }

        // -- Export Results --
        exportCsv();
        exportMarkdown();

        System.out.println("==========================================================");
        System.out.println("📊 Results exported:");
        System.out.println("   " + new java.io.File(CSV_FILE).getAbsolutePath());
        System.out.println("   " + new java.io.File(MD_FILE).getAbsolutePath());
        System.out.println("==========================================================");
    }

    // -- Cold Start: first call of each approach, zero prior access --
    private static void runColdStartTest(ProfileBean bean, CityBean cityBean) throws Exception {
        long t0 = System.nanoTime();
        // Direct access (first call)
        String directName = bean.getName();
        String directCity = bean.getDetails().getCity();
        long directNs = System.nanoTime() - t0;
        rows.add(new Row("Direct", 1, directNs, "direct"));
        System.out.printf("  Direct (cold):         %,9d ns  (name=%s, city=%s)%n", directNs, directName, directCity);

        t0 = System.nanoTime();
        // DOP cold start (factory lookup + runtime compilation on first call)
        BeanProperty<ProfileBean> dopName = BeanPropertyFactory.INSTANCE.getBeanProperty(ProfileBean.class, "name");
        BeanProperty<ProfileBean> dopDetails = BeanPropertyFactory.INSTANCE.getBeanProperty(ProfileBean.class, "details");
        BeanProperty<CityBean> dopCity = BeanPropertyFactory.INSTANCE.getBeanProperty(CityBean.class, "city");
        String dopNameVal = dopName.get(bean);
        String dopCityVal = dopCity.get(dopDetails.get(bean));
        long dopNs = System.nanoTime() - t0;
        rows.add(new Row("DOP (cold)", 1, dopNs, "dop_cold"));
        System.out.printf("  DOP (cold start):      %,9d ns  (name=%s, city=%s)%n", dopNs, dopNameVal, dopCityVal);

        t0 = System.nanoTime();
        // Reflection cold start (first Method lookup + invoke)
        Method refName = ProfileBean.class.getMethod("getName");
        Method refDetails = ProfileBean.class.getMethod("getDetails");
        Method refCity = CityBean.class.getMethod("getCity");
        String refNameVal = (String) refName.invoke(bean);
        String refCityVal = (String) refCity.invoke(refDetails.invoke(bean));
        long refNs = System.nanoTime() - t0;
        rows.add(new Row("Reflection (cold)", 1, refNs, "ref_cold"));
        System.out.printf("  Reflection (cold):     %,9d ns  (name=%s, city=%s)%n", refNs, refNameVal, refCityVal);

        t0 = System.nanoTime();
        // MethodHandle cold start (closest to C# LINQ Expression.Compile())
        java.lang.invoke.MethodHandles.Lookup lookup = java.lang.invoke.MethodHandles.lookup();
        java.lang.invoke.MethodHandle mhName = lookup.unreflect(ProfileBean.class.getMethod("getName"))
                .asType(java.lang.invoke.MethodType.methodType(Object.class, Object.class));
        java.lang.invoke.MethodHandle mhDetails = lookup.unreflect(ProfileBean.class.getMethod("getDetails"))
                .asType(java.lang.invoke.MethodType.methodType(Object.class, Object.class));
        java.lang.invoke.MethodHandle mhCity = lookup.unreflect(CityBean.class.getMethod("getCity"))
                .asType(java.lang.invoke.MethodType.methodType(Object.class, Object.class));
        String mhNameVal;
        String mhCityVal;
        try {
            mhNameVal = (String) mhName.invoke(bean);
            mhCityVal = (String) mhCity.invoke(mhDetails.invoke(bean));
        } catch (final Throwable t) {
            throw new RuntimeException(t);
        }
        long mhNs = System.nanoTime() - t0;
        rows.add(new Row("MethodHandle (cold)", 1, mhNs, "mh_cold"));
        System.out.printf("  MethodHandle (cold):   %,9d ns  (name=%s, city=%s)%n", mhNs, mhNameVal, mhCityVal);

        // Speedup ratios
        System.out.printf("%n  Speedup vs Direct:  DOP=%.0fx slower,  Reflection=%.0fx slower,  MethodHandle=%.0fx slower%n",
                (double) dopNs / Math.max(directNs, 1), (double) refNs / Math.max(directNs, 1), (double) mhNs / Math.max(directNs, 1));
        System.out.printf("  Speedup DOP vs Ref: DOP is %.1fx faster%n",
                (double) refNs / Math.max(dopNs, 1));
        System.out.printf("  Speedup MH vs Ref:  MH is %.1fx faster%n",
                (double) refNs / Math.max(mhNs, 1));
    }

    // -- Warm Test: properties already compiled, JIT may be hot --
    private static void runWarmTest(ProfileBean bean, CityBean cityBean, int N) throws Exception {
        // Pre-lookup DOP properties (already compiled from cold start phase)
        BeanProperty<ProfileBean> dopName = BeanPropertyFactory.INSTANCE.getBeanProperty(ProfileBean.class, "name");
        BeanProperty<ProfileBean> dopDetails = BeanPropertyFactory.INSTANCE.getBeanProperty(ProfileBean.class, "details");
        BeanProperty<CityBean> dopCity = BeanPropertyFactory.INSTANCE.getBeanProperty(CityBean.class, "city");

        // Pre-lookup Reflection methods
        Method refName = ProfileBean.class.getMethod("getName");
        Method refDetails = ProfileBean.class.getMethod("getDetails");
        Method refCity = CityBean.class.getMethod("getCity");

        // Pre-lookup MethodHandle (already primed from cold start phase)
        java.lang.invoke.MethodHandles.Lookup lookup = java.lang.invoke.MethodHandles.lookup();
        java.lang.invoke.MethodHandle mhName = lookup.unreflect(ProfileBean.class.getMethod("getName"))
                .asType(java.lang.invoke.MethodType.methodType(Object.class, Object.class));
        java.lang.invoke.MethodHandle mhDetails = lookup.unreflect(ProfileBean.class.getMethod("getDetails"))
                .asType(java.lang.invoke.MethodType.methodType(Object.class, Object.class));
        java.lang.invoke.MethodHandle mhCity = lookup.unreflect(CityBean.class.getMethod("getCity"))
                .asType(java.lang.invoke.MethodType.methodType(Object.class, Object.class));

        // -- Direct Access --
        long t0 = System.nanoTime();
        for (int i = 0; i < N; i++) {
            String name = bean.getName();
            CityBean details = bean.getDetails();
            String city = details.getCity();
        }
        long directNs = System.nanoTime() - t0;
        rows.add(new Row("Direct", N, directNs, "direct"));
        System.out.printf("  Direct:              %,12d ns  (avg: %8.3f ns)%n", directNs, (double) directNs / N);

        // -- DOP --
        t0 = System.nanoTime();
        for (int i = 0; i < N; i++) {
            String name = dopName.get(bean);
            CityBean details = dopDetails.get(bean);
            String city = dopCity.get(details);
        }
        long dopNs = System.nanoTime() - t0;
        rows.add(new Row("DOP", N, dopNs, "dop"));
        System.out.printf("  DOP:                 %,12d ns  (avg: %8.3f ns)%n", dopNs, (double) dopNs / N);

        // -- MethodHandle --
        t0 = System.nanoTime();
        for (int i = 0; i < N; i++) {
            try {
                String name = (String) mhName.invoke(bean);
                CityBean details = (CityBean) mhDetails.invoke(bean);
                String city = (String) mhCity.invoke(details);
            } catch (final Throwable t) {
                throw new RuntimeException(t);
            }
        }
        long mhNs = System.nanoTime() - t0;
        rows.add(new Row("MethodHandle", N, mhNs, "methodhandle"));
        System.out.printf("  MethodHandle:        %,12d ns  (avg: %8.3f ns)%n", mhNs, (double) mhNs / N);

        // -- Reflection --
        t0 = System.nanoTime();
        for (int i = 0; i < N; i++) {
            String name = (String) refName.invoke(bean);
            CityBean details = (CityBean) refDetails.invoke(bean);
            String city = (String) refCity.invoke(details);
        }
        long refNs = System.nanoTime() - t0;
        rows.add(new Row("Reflection", N, refNs, "reflection"));
        System.out.printf("  Reflection:          %,12d ns  (avg: %8.3f ns)%n", refNs, (double) refNs / N);

        // Ratios
        double directAvg = (double) directNs / N;
        double dopAvg = (double) dopNs / N;
        double mhAvg = (double) mhNs / N;
        double refAvg = (double) refNs / N;
        System.out.printf("  → DOP/Direct: %.2fx  |  MH/Direct: %.2fx  |  Ref/Direct: %.2fx  |  Ref/DOP: %.2fx  |  Ref/MH: %.2fx%n",
                dopAvg / Math.max(directAvg, 0.01), mhAvg / Math.max(directAvg, 0.01),
                refAvg / Math.max(directAvg, 0.01), refAvg / Math.max(dopAvg, 0.01), refAvg / Math.max(mhAvg, 0.01));
    }

    // -- Export CSV for graphing --
    private static void exportCsv() throws Exception {
        try (PrintWriter out = new PrintWriter(new FileWriter(CSV_FILE))) {
            out.println("test,iterations,total_ns,avg_ns,label");
            for (Row r : rows) {
                out.printf("%s,%d,%d,%.3f,%s%n", r.test, r.iterations, r.totalNs, r.avgNs, r.label);
            }
        }
    }

    // -- Export Markdown table --
    private static void exportMarkdown() throws Exception {
        try (PrintWriter out = new PrintWriter(new FileWriter(MD_FILE))) {
            out.println("# DOP vs Reflection vs Direct — Performance Benchmark\n");

            // -- Cold Start Table --
            out.println("## Cold Start (First Call)\n");
            out.println("| Approach | Time (ns) | vs Direct |");
            out.println("|----------|----------:|----------:|");
            Row coldDirect = findRow("Direct", 1, "direct");
            Row coldDop = findRow("DOP (cold)", 1, "dop_cold");
            Row coldRef = findRow("Reflection (cold)", 1, "ref_cold");
            Row coldMH = findRow("MethodHandle (cold)", 1, "mh_cold");
            if (coldDirect != null) printRow(out, "Direct access", coldDirect, coldDirect.avgNs);
            if (coldDop != null) printRow(out, "DOP (cold start)", coldDop, coldDirect != null ? coldDirect.avgNs : coldDop.avgNs);
            if (coldRef != null) printRow(out, "Reflection (cold start)", coldRef, coldDirect != null ? coldDirect.avgNs : coldRef.avgNs);
            if (coldMH != null) printRow(out, "MethodHandle (cold start)", coldMH, coldDirect != null ? coldDirect.avgNs : coldMH.avgNs);
            out.println();

            // -- Warm Iterations Table --
            out.println("## Warm Iterations (Pre-compiled)\n");
            out.println("| Iterations | Direct (avg ns) | DOP (avg ns) | DOP/Direct | Reflection (avg ns) | Ref/Direct | Ref/DOP |");
            out.println("|-----------:|----------------:|-------------:|-----------:|--------------------:|-----------:|--------:|");
            for (int n : WARM_ITERATIONS) {
                Row d = findRow("Direct", n, "direct");
                Row dp = findRow("DOP", n, "dop");
                Row r = findRow("Reflection", n, "reflection");
                if (d == null || dp == null || r == null) continue;
                out.printf("| %,10d | %,13.0f | %,10.3f | %8.2fx | %,17.0f | %8.2fx | %6.2fx |%n",
                        n, d.avgNs, dp.avgNs, dp.avgNs / Math.max(d.avgNs, 0.01),
                        r.avgNs, r.avgNs / Math.max(d.avgNs, 0.01), r.avgNs / Math.max(dp.avgNs, 0.01));
            }
        }
    }

    private static Row findRow(String test, int iterations, String label) {
        for (Row r : rows) {
            if (r.test.equals(test) && r.iterations == iterations && r.label.equals(label)) return r;
        }
        return null;
    }

    private static void printRow(PrintWriter out, String name, Row row, double baselineAvg) {
        double ratio = row.avgNs / Math.max(baselineAvg, 1);
        out.printf("| %-24s | %,10.0f | %.2fx |%n", name, row.avgNs, ratio);
    }
}
