package cs3205.db.restapi.team2.services;

// import cs3205.db.restapi.team2.DatabaseConnector;
import utils.db.MySQLAccess;

public class ResearcherService {
	// private static String dbUser = "team2";
	// private static String dbPassword = "FilletOFishWithChilliSauce789!";
	// DatabaseConnector db;

	public ResearcherService() {
		// DatabaseConnector db = new DatabaseConnector(dbUser, dbPassword);
		MySQLAccess.connectDatabase();
	}

}
