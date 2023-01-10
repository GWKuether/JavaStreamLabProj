package com.dcc.jpa_stream_lab.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dcc.jpa_stream_lab.repository.ProductsRepository;
import com.dcc.jpa_stream_lab.repository.RolesRepository;
import com.dcc.jpa_stream_lab.repository.ShoppingcartItemRepository;
import com.dcc.jpa_stream_lab.repository.UsersRepository;
import com.dcc.jpa_stream_lab.models.Product;
import com.dcc.jpa_stream_lab.models.Role;
import com.dcc.jpa_stream_lab.models.ShoppingcartItem;
import com.dcc.jpa_stream_lab.models.User;

@Transactional
@Service
public class StreamLabService {
	
	@Autowired
	private ProductsRepository products;
	@Autowired
	private RolesRepository roles;
	@Autowired
	private UsersRepository users;
	@Autowired
	private ShoppingcartItemRepository shoppingcartitems;


    // <><><><><><><><> R Actions (Read) <><><><><><><><><>

    public List<User> RDemoOne() {
    	// This query will return all the users from the User table.
    	return users.findAll().stream().toList();
    }

    public long RProblemOne()
    {
        // Return the COUNT of all the users from the User table.
        // You MUST use a .stream(), don't listen to the squiggle here!
        // Remember yellow squiggles are warnings and can be ignored.
    	return users.findAll().stream().count();
    }

    public List<Product> RDemoTwo()
    {
        // This query will get each product whose price is greater than $150.
    	return products.findAll().stream().filter(p -> p.getPrice() > 150).toList();
    }

    public List<Product> RProblemTwo()
    {
        // Write a query that gets each product whose price is less than or equal to $100.
        // Return the list
        return products.findAll().stream().filter(p -> p.getPrice() <= 100).toList();
    }

    public List<Product> RProblemThree()
    {
        // Write a query that gets each product that CONTAINS an "s" in the products name.
        // Return the list
    	return products.findAll().stream().filter(p -> p.getName().contains("s")).toList();
    }

    public List<User> RProblemFour()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, Calendar.JANUARY, 01, 00, 00, 00);
        Date date = calendar.getTime();
        // Write a query that gets all the users who registered BEFORE 2016
        // Return the list
        // Research 'java create specific date' and 'java compare dates'
        // You may need to use the helper classes imported above!
    	
        return users.findAll().stream().filter(p -> p.getRegistrationDate().before(date)).toList();
    }

    public List<User> RProblemFive()
    {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2016, Calendar.JANUARY, 01,00,00,00);
        Date date1 = calendar1.getTime();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2018, Calendar.JANUARY, 01,00,00,00);
        Date date2 = calendar2.getTime();

        // Write a query that gets all of the users who registered AFTER 2016 and BEFORE 2018
        // Return the list

        return users.findAll().stream().filter(p -> p.getRegistrationDate().after(date1) && p.getRegistrationDate().before(date2)).toList();
    }

    // <><><><><><><><> R Actions (Read) with Foreign Keys <><><><><><><><><>

    public List<User> RDemoThree()
    {
        // Write a query that retrieves all of the users who are assigned to the role of Customer.
    	Role customerRole = roles.findAll().stream().filter(r -> r.getName().equals("Customer")).findFirst().orElse(null);
    	List<User> customers = users.findAll().stream().filter(u -> u.getRoles().contains(customerRole)).toList();

    	return customers;
    }

    public List<Product> RProblemSix()
    {

        // Write a query that retrieves all of the products in the shopping cart of the user who has the email "afton@gmail.com".
        // Return the list
        User customer =  users.findAll().stream().filter(p ->p.getEmail().equals("afton@gmail.com")).findFirst().orElse(null);
        List<Product> userItems = shoppingcartitems.findAll().stream().filter(p -> p.getUser().equals(customer)).map(p -> p.getProduct()).toList();
        return userItems;

    }

    public long RProblemSeven()
    {
        // Write a query that retrieves all of the products in the shopping cart of the user who has the email "oda@gmail.com" and returns the sum of all of the products prices.
    	// Remember to break the problem down and take it one step at a time!
        User oda = users.findAll().stream().filter(p ->p.getEmail().equals("oda@gmail.com")).findFirst().orElse(null);
        List<ShoppingcartItem> odasCart = shoppingcartitems.findAll().stream().filter(p ->p.getUser().equals(oda)).toList();
        List<Product> odasProducts = odasCart.stream().map(p ->p.getProduct()).toList();

        long result = 0;
        for (Product p : odasProducts){
            result += p.getPrice();
        }
        return result;
    }

    public List<Product> RProblemEight()
    {
//         Write a query that retrieves all of the products in the shopping cart of users who have the role of "Employee".
//    	// Return the list
        Role roll = roles.findAll().stream().filter(r->r.getName().equals("Employee")).findFirst().orElse(null);
        List<User> employees = users.findAll().stream().filter(p ->p.getRoles().contains(roll)).toList();
//        List<ShoppingcartItem> employeeItems = shoppingcartitems.findAll().stream().filter(p ->p.getUser().equals(employees)).toList();
//
        List<Product> employeeProductList = new ArrayList<Product>();
        for (User user : employees){
            List<Product> products = shoppingcartitems.findAll().stream().filter(p ->p.getUser().equals(user)).map(s->s.getProduct()).toList();
           employeeProductList.addAll(products);
        }

    	return employeeProductList;
    }

    // <><><><><><><><> CUD (Create, Update, Delete) Actions <><><><><><><><><>

    // <><> C Actions (Create) <><>

    public User CDemoOne()
    {
        // Create a new User object and add that user to the Users table.
        User newUser = new User();        
        newUser.setEmail("david@gmail.com");
        newUser.setPassword("DavidsPass123");
        users.save(newUser);
        return newUser;
    }

    public Product CProblemOne()
    {
        // Create a new Product object and add that product to the Products table.
        // Return the product
        Product newProduct = new Product();
        newProduct.setName("Gatorade");
        newProduct.setDescription("The most delicious drink known to man!");
        newProduct.setPrice(3);
        products.save(newProduct);
    	return newProduct;

    }

    public List<Role> CDemoTwo()
    {
        // Add the role of "Customer" to the user we just created in the UserRoles junction table.
    	Role customerRole = roles.findAll().stream().filter(r -> r.getName().equals("Customer")).findFirst().orElse(null);
    	User david = users.findAll().stream().filter(u -> u.getEmail().equals("david@gmail.com")).findFirst().orElse(null);
    	david.addRole(customerRole);
    	return david.getRoles();
    }

    public ShoppingcartItem CProblemTwo()
    {
    	// Create a new ShoppingCartItem to represent the new product you created being added to the new User you created's shopping cart.
        // Add the product you created to the user we created in the ShoppingCart junction table.
        // Return the ShoppingcartItem

        User david = users.findAll().stream().filter(p ->p.getEmail().equals("david@gmail.com")).findFirst().orElse(null);
        Product gatorade = products.findAll().stream().filter(p ->p.getName().equals("Gatorade")).findFirst().orElse(null);
        ShoppingcartItem newShoppingcartItem = new ShoppingcartItem();
        newShoppingcartItem.setUser(david);
        newShoppingcartItem.setProduct(gatorade);
        newShoppingcartItem.setQuantity(1);
        shoppingcartitems.save(newShoppingcartItem);

    	return newShoppingcartItem;
    	
    }

    // <><> U Actions (Update) <><>

    public User UDemoOne()
    {
         //Update the email of the user we created in problem 11 to "mike@gmail.com"
          User user = users.findAll().stream().filter(u -> u.getEmail().equals("david@gmail.com")).findFirst().orElse(null);
          user.setEmail("mike@gmail.com");
          return user;
    }

    public Product UProblemOne()
    {
        // Update the price of the product you created to a different value.
        // Return the updated product
        Product product = products.findAll().stream().filter(p ->p.getName().equals("Gatorade")).findFirst().orElse(null);
        product.setPrice(4);
    	return product;
    }

    public User UProblemTwo()
    {
        // Change the role of the user we created to "Employee"
        // HINT: You need to delete the existing role relationship and then create a new UserRole object and add it to the UserRoles table
        User david = users.findAll().stream().filter(p ->p.getEmail().equals("mike@gmail.com")).findFirst().orElse(null);
        Role davidsRole = roles.findAll().stream().filter(p ->p.getUsers().equals(david)).findFirst().orElse(null);
        roles.delete(davidsRole);
        davidsRole.setName("Employee");
    	return david;
    }

    //BONUS:
    // <><> D Actions (Delete) <><>

    // For these bonus problems, you will also need to create their associated routes in the Controller file!
    
    // DProblemOne
    // Delete the role relationship from the user who has the email "oda@gmail.com".

    // DProblemTwo
    // Delete all the product relationships to the user with the email "oda@gmail.com" in the ShoppingCart table.

    // DProblemThree
    // Delete the user with the email "oda@gmail.com" from the Users table.

}
