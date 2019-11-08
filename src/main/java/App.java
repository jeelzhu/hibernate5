import entity.Course;
import entity.Student;
import util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class App {
	public static void main(String[] args) {

		Student student = new Student("Ramesh", "Fadatare", "rameshfadatare@javaguides.com");
		Student student1 = new Student("John", "Cena", "john@javaguides.com");

		List<Course> courseList = new ArrayList<>();
		Course course = new Course();
		course.setName("test");
		course.setStudent(student);
		courseList.add(course);
		student.setCourses(courseList);

		List<Course> courseList1 = new ArrayList<>();
		Course course1 = new Course();
		course1.setName("test1");
		course1.setStudent(student1);
		courseList1.add(course1);
		student1.setCourses(courseList1);

		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();
			// save the student objects
			session.save(student);
			for (Course cours : student.getCourses()) {
				session.save(cours);
				session.save(student1);
			}
			for (Course cours : student1.getCourses()) {
				session.save(cours);
			}
			// commit transaction
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			List<Student> students = session.createQuery("from Student", Student.class).list();
			students.forEach(s -> {
				System.out.println("Print student email id : " + s.getEmail());
				System.out.println("222 : " + s.getCourses().size());
			});
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}
}
