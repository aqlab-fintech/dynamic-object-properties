import com.aqlab.dynamicobjectproperties.ObjectProperty;
import com.aqlab.dynamicobjectproperties.BeanPropertyFactory;
import java.util.ArrayList;
import java.util.List;

// Helper Bean
class SimpleBean {
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

// Bean that contains another bean
class ProfileBean {
    private SimpleBean owner;

    public SimpleBean getOwner() { return owner; }
    public void setOwner(SimpleBean owner) { this.owner = owner; }
}

public class ChainedPropertyExample {
    public static void main(String[] args) {
        // Setup the main bean
        ProfileBean profile = new ProfileBean();
        SimpleBean user = new SimpleBean();
        user.setName("Dave");
        user.setAge(55);
        user.setActive(true);
        profile.setOwner(user);

        // 1. Get the property for the nested bean
        ObjectProperty<SimpleBean> ownerProp = BeanPropertyFactory.INSTANCE.getBeanProperty(ProfileBean.class, "owner");

        if (ownerProp != null) {
            // 2. Access the nested bean instance
            SimpleBean owner = ownerProp.get(profile);
            System.out.println("\n--- Chained Property Access --- ");
            System.out.println("Owner Name: " + owner.getName());
            
            // 3. Get the property *on the nested bean* using the parent's property factory
            ObjectProperty<Integer> ageProp = BeanPropertyFactory.INSTANCE.getBeanProperty(SimpleBean.class, "age");

            // 4. Access the value through the nested property accessor
            int age = ageProp.get(owner);
            System.out.println("Owner Age: " + age);

            // 5. Modify the nested bean's property
            ageProp.set(owner, 56);
            System.out.println("\nAfter setting nested age to 56:");
            System.out.println("New Owner Age: " + ageProp.get(owner));
        } else {
            System.out.println("Error: 'owner' property not found!");
        }
    }
}

