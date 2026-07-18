import com.aqlab.dynamicobjectproperties.ObjectProperty;
import com.aqlab.dynamicobjectproperties.BeanPropertyFactory;
import java.util.ArrayList;
import java.util.List;

// Helper Bean (like the one in SimpleBeanExample)
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

// Bean that holds a List of SimpleBeans
class UserCollectionBean {
    private List<SimpleBean> users = new ArrayList<>();

    public List<SimpleBean> getUsers() { return users; }
    public void setUsers(List<SimpleBean> users) { this.users = users; }
}

public class ListBackedExample {
    public static void main(String[] args) {
        // Setup the bean
        UserCollectionBean manager = new UserCollectionBean();
        manager.setUsers(List.of(
            new SimpleBean(){{ setName("Bob"); setAge(25); setActive(true); }},
            new SimpleBean(){{ setName("Charlie"); setAge(40); setActive(false); }}
        ));

        // Get the property for the list itself
        ObjectProperty<List<SimpleBean>> usersProperty = BeanPropertyFactory.INSTANCE.getBeanProperty(UserCollectionBean.class, "users");

        if (usersProperty != null) {
            // Access the list
            List<SimpleBean> userList = usersProperty.get(manager);
            System.out.println("\n--- List Backed Access --- ");
            System.out.println("Total Users: " + userList.size());

            // Access element by index (fastest path!)
            if (!userList.isEmpty()) {
                SimpleBean firstUser = userList.get(0);
                System.out.println("First User Name: " + firstUser.getName());
                
                // Access property *within* the list element
                ObjectProperty<String> nameProp = BeanPropertyFactory.INSTANCE.getBeanProperty(SimpleBean.class, "name");
                String firstName = nameProp.get(firstUser);
                System.out.println("First User Name (via List Access): " + firstName);

                // Modify element within the list
                ObjectProperty<Integer> ageProp = BeanPropertyFactory.INSTANCE.getBeanProperty(SimpleBean.class, "age");
                ageProp.set(firstUser, 26);
                System.out.println("\nAfter setting age to 26:");
                System.out.println("First User New Age: " + ageProp.get(firstUser));
            }

            // Test setter for the whole list
            List<SimpleBean> updatedList = List.of(
                new SimpleBean(){{ setName("Alice"); setAge(32); setActive(true); }},
                new SimpleBean(){{ setName("Charlie"); setAge(41); setActive(false); }}
            );
            usersProperty.set(manager, updatedList);
            System.out.println("\nSuccessfully updated list. New count: " + usersProperty.get(manager).size());

        } else {
            System.out.println("Error: 'users' property not found!");
        }
    }
}

