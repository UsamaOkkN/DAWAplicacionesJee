import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class FiltroConsultaServlet extends HttpServlet {
	// El método doGet() se ejecuta una vez por cada petición HTTP GET.
	private String userName;
	private String password;
	private String url;
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Establecemos el tipo MIME del mensaje de respuesta
		response.setContentType("text/html");
		// Creamos un objeto para poder escribir la respuesta
		PrintWriter out = response.getWriter();
		Connection conn = null;
		Statement stmt = null;
		try {
			//Paso 1: Cargar el driver JDBC
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			
			
			conn = DriverManager.getConnection(url, userName, password);
			
			//Paso 3: Crear sentencias SQL, utilizando objetos de tipo Statement
			stmt = conn.createStatement();
			
			//Paso 4: Ejecutar las sentencias SQL a través de los objetos Statement
			String sqlStr = "select * from libros where autor = "
					+ "'" + request.getParameter("autor") + "'";
			
			//Generar una página HTML como resultado de la consulta
			out.println("<html><head><title>Resultado de la consulta</title></head><body>");
			out.println("<h3>Gracias por su consulta.</h3>");
			out.println("<p>Tu consulta es: " + sqlStr + "</p>");
			ResultSet rset = stmt.executeQuery(sqlStr);
			
			//Paso 5: Procesar el conjunto de registros resultante utilizando ResultSet
			int count = 0;
			while(rset.next()) {
				out.println("<p>" + rset.getString("autor")
				+ ", " + rset.getString("titulo")
				+ ", " + rset.getDouble("precio") + "</p>");
				
				count++;
			}
			out.println("<p>=== " + count + " registros encontrados =====</p>");
			out.println("</body></html>");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			out.close();   // Cerramos el flujo de escritura
			try {
				// Cerramos el resto de los recursos
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
		userName=config.getInitParameter("usuario");
		password = config.getInitParameter("password");
		url = config.getInitParameter("URLBaseDeDatos");
	}

}
