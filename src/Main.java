import com.hb.tutorial.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;


public class Main {

    public static void main(String args[]) {
        SessionFactory factory = initSessionFactory();

        Session session = factory.getCurrentSession();

        try {
            session.beginTransaction();

            printMessage("Creating entities");
            createEntities(session);
            session.getTransaction().commit();

            session = factory.getCurrentSession();
            session.beginTransaction();
            printMessage("Read entities");
            readAllEntities(session);
            session.getTransaction().commit();

            session = factory.getCurrentSession();
            session.beginTransaction();
            printMessage("Updating some entities");
            updateEntities(session);
            session.getTransaction().commit();

            session = factory.getCurrentSession();
            session.beginTransaction();
            printMessage("Deleting some entities");
            deleteEntities(session);
            session.getTransaction().commit();
        }
        finally {
            session.close();
            factory.close();
        }
    }

    private static SessionFactory initSessionFactory() {
        return new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor.class)
                .addAnnotatedClass(InstructorDetail.class)
                .addAnnotatedClass(Course.class)
                .addAnnotatedClass(Review.class)
                .addAnnotatedClass(Student.class)
                .buildSessionFactory();
    }

    private static void createEntities(Session session) {
        Instructor firstInstructor = new Instructor("Cliff", "Berton", "cliff@mail.com",
                                     new InstructorDetail("cliffChannel", "bass guitar playing"));
        Instructor secondInstructor = new Instructor("James", "Hetfield", "james@mail.com",
                                      new InstructorDetail("jamesChannel", "lead guitar playing"));

        Course firstCourse = new Course("Bass playing");
        Course secondCourse = new Course("Guitar playing");

        firstInstructor.addCourse(firstCourse);
        firstInstructor.addCourse(secondCourse);
        secondInstructor.addCourse(secondCourse);

        Student firstStudent = new Student("Kerry", "King", "kerry@mail.com");
        Student secondStudent = new Student("Steve", "Vai", "steve@mail.com");

        firstCourse.addStudent(firstStudent);
        firstCourse.addStudent(secondStudent);
        secondCourse.addStudent(secondStudent);

        Review firstReview = new Review("Best course");
        Review secondReview = new Review("Just waste time");

        firstCourse.addReview(firstReview);
        secondCourse.addReview(secondReview);

        session.save(firstInstructor);
        session.save(secondInstructor);
        session.save(firstCourse);
        session.save(secondCourse);
        session.save(firstStudent);
        session.save(secondStudent);
        session.save(firstReview);
        session.save(secondReview);
    }

    private static void printMessage(String message) {
        System.out.println("\t\t---------------" + message + "---------------" );
    }

    private static void printList(List list, String message) {
        printMessage(message);
        for (Object o : list)
            System.out.println(o + "\n");
    }

    private static void readAllEntities(Session session) {
        List<Instructor> instructors = (List<Instructor>)session.createQuery("from Instructor").list();
        printList(instructors, "Instructors");

        List<Course> courses = (List<Course>)session.createQuery("from Course").list();
        printList(courses, "Courses");

        List<Student> students = (List<Student>)session.createQuery("from Student").list();
        printList(students, "Students");

    }

    private static void updateEntities(Session session) {
        Instructor instructor = (Instructor)session.createQuery("from Instructor where firstName='James'").uniqueResult();
        instructor.setFirstName("Jimi");
        instructor.setLastName("Hendrix");

        Course course = (Course)session.createQuery("from Course where title='Bass playing'").uniqueResult();
        course.setTitle("Bass guitar playing");
    }

    private static void deleteEntities(Session session) {
        Instructor instructor = (Instructor)session.createQuery("from Instructor where firstName='Cliff'").uniqueResult();
        session.delete(instructor);

        Course course = (Course)session.createQuery("from Course where title='Guitar playing'").uniqueResult();
        session.delete(course);

        Student student = (Student)session.createQuery("from Student where firstName='Steve'").uniqueResult();
        session.delete(student);
    }
}
