package scattlabs.jaxrs.jersey.service;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONObject;

import scattlabs.jaxrs.jersey.common.MasterConnection;
import scattlabs.jaxrs.jersey.common.MyMap;
import scattlabs.jaxrs.jersey.model.Student;

@Path("/student")
public class StudentService extends MasterConnection {

	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Student getStudent() {
		Student s = new Student();

		s.setId("143040001");
		s.setName("John Doe");
		s.setAddress("Gotham City");

		return s;
	}

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createStudent(Student student) {
		String respon = "Data Saved : " + student;

		return Response.status(201).entity(respon).build();
	}

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAll")
	public Object getAllBarang(@Context UriInfo uriInfo, @Context HttpServletRequest hsr) {
		createConnection();
		List<MyMap> listBarang = jt.queryList("select * from student", new MyMap());
		closeConnection();

		return listBarang;
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> getStudentByID(@PathParam("id") Integer id) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("statusId", "1");
		result.put("message", "INQUIRY BERHASIL");
		System.out.println("id : " + id);
		try {
			createConnection();
			MyMap student = (MyMap) jt.queryObject("select * from student where id=?", new Object[] { id },
					new MyMap());
			closeConnection();
			if (student != null) {
				result.put("result", student);
			}
		} catch (Exception e) {
			result.put("message", "GAGAL KARENA : " + e.getMessage());
			// TODO: handle exception
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/insert")
	public Object createStd(@Context HttpServletRequest hsr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		JSONObject request = null;
		MyMap respon = new MyMap();
		MyMap data = new MyMap();
		DataInputStream in;
		try {
			createConnection();
			System.out.println("hrs : " + hsr.getInputStream());
			in = new DataInputStream(new BufferedInputStream(hsr.getInputStream()));

			while ((line = in.readLine()) != null)
				sb.append(line);
			JSONObject json = new JSONObject(sb.toString());
			request = (JSONObject) json.get("request");

			if (request == null) {
				respon.put("Message", "Data Yang dikirim tidak ditemukan");
				respon.put("rCode", "99");
				respon.put("statusId", "0");
				return respon;
			}
			data.put("name", request.getString("name"));
			data.put("address", request.getString("address"));

			jt.insert("student", data);
			respon.put("message", "Data Berhasil Disimpan");
			respon.put("rCode", "00");
			respon.put("statusId", "1");
		} catch (Exception e) {
			e.printStackTrace();
			respon.put("message", e.getMessage());
			respon.put("rCode", "99");
			respon.put("statusId", "0");
			// TODO: handle exception
		}
		return respon;
	}

}
