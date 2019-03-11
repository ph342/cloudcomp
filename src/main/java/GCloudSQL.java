
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@WebListener("Creates a connection pool that is stored in the Servlet's context for later use.")
public final class GCloudSQL implements ServletContextListener {
	
	public static final String conn = "db-connection";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:postgresql://google/postgres?useSSL=false");
		config.setUsername("postgres");
		config.setPassword("postgres");
		config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.postgres.SocketFactory");
		config.addDataSourceProperty("cloudSqlInstance", "cloudcwk:europe-west1:shopdb");

		config.setMaximumPoolSize(5);
		config.setMinimumIdle(5);
		config.setConnectionTimeout(10000); // 10 seconds
		config.setIdleTimeout(600000); // 10 minutes
		config.setMaxLifetime(1800000); // 30 minutes

		sce.getServletContext().setAttribute(conn, new HikariDataSource(config));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		HikariDataSource pool = (HikariDataSource) sce.getServletContext().getAttribute(conn);
		if (pool != null) {
			pool.close();
		}
	}
}
