package tfip_project.financial_analysis.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

@Configuration
public class GCalendarConfig {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String SERVICE_ACCOUNT_FILE_PATH = "src/main/resources/trading-journal-sb-d201ecfdf95b.json";

    @Bean
    @Scope("singleton")
    public Calendar getCalendarService() throws IOException, GeneralSecurityException {
        
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // Load the service account credentials from the JSON key file
        GoogleCredentials credentials = ServiceAccountCredentials
                        .fromStream(new FileInputStream(SERVICE_ACCOUNT_FILE_PATH))
                        .createScoped(Collections.singleton(CalendarScopes.CALENDAR));

        // Create an HTTP credentials adapter
        HttpCredentialsAdapter credentialsAdapter = new HttpCredentialsAdapter(credentials);

        return new Calendar.Builder(httpTransport, JSON_FACTORY, credentialsAdapter)
                            .setApplicationName("Trading Journal")
                            .build();
    }
}
