import com.aqlab.dynamicobjectproperties.ObjectProperty;
import com.aqlab.dynamicobjectproperties.BeanPropertyFactory;
import java.util.Map;

public class SimpleBeanExample {
    // 1. Simple data class with standard Java getters/setters
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
        // Instantiate the bean
        SimpleBean user = new SimpleBean();
        user.setName(Alice);
        user.setAge(30);
        user.setActive(true);

        // Get the property for 'age'
        ObjectProperty<Integer> ageProperty = BeanPropertyFactory.INSTANCE.getBeanProperty(SimpleBean.class, age);

        if (ageProperty != null) {
            // Access value at compile-time speed
            int age = ageProperty.get(user);
            System.out.println("--- Simple Bean Access --- ");
            System.out.println("Name: " + ageProperty.get(user).getName());
            System.out.println("Age: " + age);
            System.out.println("Is Active: " + ageProperty.isReadable() + " (can get)");

            // Modify value at compile-time speed
            ageProperty.set(user, 31);
            System.out.println("\nAfter setting age to 31:");
            System.out.println("New Age: " + ageProperty.get(user));

            // Test specific primitive accessor (fastest path)
            System.out.println("Can get Age? " + ageProperty.canGetInt());
        } else {
            System.out.println("Error: 'age' property not found!");
        }
    }
}

