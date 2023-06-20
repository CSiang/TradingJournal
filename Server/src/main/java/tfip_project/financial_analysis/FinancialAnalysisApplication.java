package tfip_project.financial_analysis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tfip_project.financial_analysis.Security.Models.AppUser;
import tfip_project.financial_analysis.Security.Models.Role;
import tfip_project.financial_analysis.Services.EmailService;
import tfip_project.financial_analysis.Services.TradeRecordService;
import tfip_project.financial_analysis.Services.UserService;
import tfip_project.financial_analysis.Services.yhFinanceService;
import yahoofinance.Stock;

@SpringBootApplication
public class FinancialAnalysisApplication implements CommandLineRunner {

	@Autowired
	yhFinanceService yhSvc;

	@Autowired
	UserService userSvc;

	@Autowired
	TradeRecordService trSvc;

	@Autowired
	private EmailService emailSvc;

	public static void main(String[] args) {
		SpringApplication.run(FinancialAnalysisApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Role role1 = new Role();
		// role1.setName("ADMIN");
		// userSvc.saveRole(role1);

		// Role role2 = new Role();
		// role2.setName("USER");
		// userSvc.saveRole(role2);

		// AppUser user = new AppUser();
		// user.setEmail("cscorpio87@gmail.com");
		// // user.setId(Long.valueOf("1"));
		// user.setName("Chee Siang");
		// user.setPassword("cscorpio87");
		// user.getRoles().add(role1);
		// user.getRoles().add(role2);
		// user.setUsername("CS");

		// userSvc.saveUser(user);

		// System.out.printf(
		// 	"Record count for CS holding is true: %d\n",
		// 	trSvc.getRecordCount(true, "CS")
		// );

		// System.out.println("Records for CS:");
		// System.out.println(trSvc.getRecords(true, "CS", 10, 0));

		// System.out.printf("The portfolio:\n%s", trSvc.getPortfolio("CS"));
		// String yahooResult = yhSvc.getStockPrice();
		// System.out.printf("The stock price Json is:\n%s", yahooResult);

		// String message = """
		// Follow the link below to reset your password:
		// http://localhost:4200/login
		// 		""";
		// emailSvc.sendSimpleMessage("cscorpio87@gmail.com", "Testing", message);

	}

}
