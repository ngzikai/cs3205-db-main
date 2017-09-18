package mysql;

import java.time.LocalDate;

import org.json.JSONObject;

import controller.*;
import entity.User;

public class SqlTest {
	public static void main(String[] args) throws Exception {
		//MySQLAccess dao = new MySQLAccess();
		//dao.useDataBase();

		testUserController();
		testUserControllerCreate();
		//testUserControllerDelete();
	}


	public static void testAdminController() {
		String adminuser = "suser";
		AdminController ac = new AdminController();
		//ac.getAdmin(adminuser).print();

	}

	public static void testUserController() {

		UserController uc = new UserController();

		String userName = "Alan88";
		JSONObject user = uc.getUser(userName);
		if(user == null) {
			System.out.println("No such user found");
		} else {
			System.out.println(user);;
		}

	}

	public static void testUserControllerCreate() {

		UserController uc = new UserController();

		/*
		String[] phone = {"222", "111", "333"};
		String[] address = {"KR", "PGP", "VIVO"};
		int[] zipcode = { 543211, 5433333, 546666};
		User newUser = new User("Alan88", "hellop", "salt", "Alan", "Homie", "S1231231B", LocalDate.parse("2015-12-17"),
				'M', phone, address,zipcode, 0, "A+", "111") ;
		System.out.println(uc.createUser(newUser));*/

		//String user = "Bob99";
		String user = "Alan88";
		uc.getUser(user);

	}



	public static void testUserControllerDelete() {
		UserController uc = new UserController();
		int uid = 2;
//		uc.deleteUser(uid).print();
	}

}
