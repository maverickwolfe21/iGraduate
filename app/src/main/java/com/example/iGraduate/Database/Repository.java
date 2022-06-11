package com.example.iGraduate.Database;

//calls DAO

import android.app.Application;

import com.example.iGraduate.DAO.AssessmentDAO;
import com.example.iGraduate.DAO.CourseDAO;
import com.example.iGraduate.DAO.InstructorDAO;
import com.example.iGraduate.DAO.TermDAO;
import com.example.iGraduate.Entity.Assessment;
import com.example.iGraduate.Entity.Course;
import com.example.iGraduate.Entity.Instructor;
import com.example.iGraduate.Entity.Term;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Repository {
    private CourseDAO mCourseDao;
    private InstructorDAO mInstructorDao;
    private AssessmentDAO mAssessmentDao;
    private TermDAO mTermDao;

    private List<Course> mAllCourses;
    private List<Assessment> mAllAssessments;
    private List<Instructor> mAllInstructors;
    private List<Term> mAllTerms;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        DatabaseBuilder db = DatabaseBuilder.getDatabase(application);
        mCourseDao = db.courseDAO();
        mInstructorDao = db.instructorDAO();
        mAssessmentDao = db.assessmentDAO();
        mTermDao = db.termDAO();
    }

    //INSERTS
    public void insertCourse(Course course) {
        //second thread
        databaseExecutor.execute(() -> {
            mCourseDao.insert(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //INSERTS
    public void insertInstructor(Instructor instructor) {
        //second thread
        databaseExecutor.execute(() -> {
            mInstructorDao.insert(instructor);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //INSERTS
    public void insertAssessment(Assessment assessment) {

        //System.out.println("Assessment" + assessment);
        //second thread
        databaseExecutor.execute(() -> {
            mAssessmentDao.insert(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //INSERTS
    public void insertTerm(Term term) {

        //System.out.println("Assessment" + assessment);
        //second thread
        databaseExecutor.execute(() -> {
            mTermDao.insert(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //search
    public Course findCourse(int courseId) {

        Course course = new Course();

        //second thread
        databaseExecutor.execute(() -> {
            Course course1 = mCourseDao.findCourse(courseId);
            //System.out.println("DB Instructor " + instructor1);
            course.setCourseId(course1.getCourseId());
            course.setTitle(course1.getTitle());
            course.setInstructorIds(course1.getInstructorIds());
            course.setAssessmentIds(course1.getAssessmentIds());
            course.setStartDate(course1.getStartDate());
            course.setShouldRemindStart(course1.getShouldRemindStart());
            course.setShouldRemindEnd(course1.getShouldRemindEnd());
            course.setEndDate(course1.getEndDate());
            course.setStatus(course1.getStatus());
            course.setNotes(course1.getNotes());
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return course;
    }

    //search
    public Instructor findInstructor(int instructorId) {

        Instructor instructor = new Instructor();

        //second thread
        databaseExecutor.execute(() -> {
            Instructor instructor1 = mInstructorDao.findInstructor(instructorId);
            //System.out.println("DB Instructor " + instructor1);
            instructor.setInstructorId(instructor1.getInstructorId());
            instructor.setName(instructor1.getName());
            instructor.setEmail(instructor1.getEmail());
            instructor.setPhone(instructor1.getPhone());
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return instructor;
    }

    //search
    public Assessment findAssessment(int assessmentId) {

        //AtomicBoolean isNull = new AtomicBoolean(false);
        Assessment assessment = new Assessment();

        //second thread
        databaseExecutor.execute(() -> {
            Assessment assessment1 = mAssessmentDao.findAssessment(assessmentId);
            //System.out.println("Assessment " + assessment1);
            assessment.setAssessmentId(assessment1.getAssessmentId());
            assessment.setAssessmentId(assessment1.getAssessmentId());
            assessment.setTitle(assessment1.getTitle());
            assessment.setStartDate(assessment1.getStartDate());
            assessment.setEndDate(assessment1.getEndDate());
            assessment.setType(assessment1.getType());

        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return assessment;

    }

    //search
    public Term findTerm(int termId) {

        Term term = new Term();

        //second thread
        databaseExecutor.execute(() -> {
            Term term1 = mTermDao.findTerm(termId);
            //System.out.println("Found Term " + term1);
            term.setTermId(term1.getTermId());
            term.setTitle(term1.getTitle());
            term.setStartDate(term1.getStartDate());
            term.setEndDate(term1.getEndDate());
            term.setCourseIds(term1.getCourseIds());
        });
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return term;
    }

    //UPDATES
    public void updateCourse(Course course) {
        //second thread
        databaseExecutor.execute(() -> {
            mCourseDao.updateCourse(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //UPDATES
    public void updateAssessment(Assessment assessment) {
        //second thread
        databaseExecutor.execute(() -> {
            mAssessmentDao.updateAssessment(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //UPDATES
    public void updateTerm(Term term) {
        //second thread
        databaseExecutor.execute(() -> {
            mTermDao.updateTerm(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateInstructor(Instructor instructor) {
        //second thread
        databaseExecutor.execute(() -> {
            mInstructorDao.updateInstructor(instructor);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //DELETE
    public void deleteCourse(int courseId) {
        //second thread
        databaseExecutor.execute(() -> {
            mCourseDao.delete(courseId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //DELETE
    public void deleteTerm(int termId) {
        //second thread
        databaseExecutor.execute(() -> {
            mTermDao.delete(termId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteAssessment(int assessmentId) {
        //second thread
        databaseExecutor.execute(() -> {
            mAssessmentDao.delete(assessmentId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteInstructor(int instructorId) {
        //second thread
        databaseExecutor.execute(() -> {
            mInstructorDao.delete(instructorId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //GETTERS
    public List<Course> getAllCourses() {
        databaseExecutor.execute(() -> {
            mAllCourses = mCourseDao.getAllCourses();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllCourses;
    }

    //GETTERS
    public List<Assessment> getAllAssessments() {
        databaseExecutor.execute(() -> {
            mAllAssessments = mAssessmentDao.getAllAssessments();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllAssessments;
    }

    //GETTERS
    public List<Instructor> getAllInstructors() {
        databaseExecutor.execute(() -> {
            mAllInstructors = mInstructorDao.getAllInstructors();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllInstructors;
    }

    //GETTERS
    public List<Term> getAllTerms() {
        databaseExecutor.execute(() -> {
            mAllTerms = mTermDao.getAllTerms();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllTerms;
    }

    public void deleteAll() {
        databaseExecutor.execute(() -> {
            mCourseDao.deleteAll();
            mInstructorDao.deleteAll();
            mAssessmentDao.deleteAll();
            mTermDao.deleteAll();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resetAutoIncrements() {
        databaseExecutor.execute(() -> {
            mCourseDao.resetAutoIncrement();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
