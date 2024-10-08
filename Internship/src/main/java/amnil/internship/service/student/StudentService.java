package amnil.internship.service.student;

import amnil.internship.dto.request.UpdateStudentRequest;
import amnil.internship.exception.RecordNotFoundException;
import amnil.internship.model.Student;
import amnil.internship.model.User;
import amnil.internship.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StudentService implements IStudentService {

    public final StudentRepository studentRepository;
    public final PasswordEncoder passwordEncoder;

    @Override
    public Student registerStudent(Student student) {
        Optional<Student> existingStudent = studentRepository.findByRoll(student.getRoll());
        if (existingStudent.isPresent()) {
            throw new RecordNotFoundException("Student with roll number " + student.getRoll() + " already exists");
        }
        String password = student.getUser().getCredentials().getPassword();
        student.getUser().getCredentials().setPassword(passwordEncoder.encode(password));

        return studentRepository.findByRoll(student.getRoll())
                .orElseGet(() -> studentRepository.save(student));
    }

    @Override
    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Record Not Found for " + id));
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.findById(id).ifPresentOrElse(studentRepository::delete, () -> {
            throw new RecordNotFoundException("Student Doesn't exist");
        });
    }

    @Override
    public Student updateStudent(UpdateStudentRequest student, Long id) {
//        System.out.println("Looking for student with ID: " + id);

        return studentRepository.findById(id)
                .map(existingStudent -> {
//                    System.out.println("Looking for student with ID: " + id);
                    return updateExistingStudent(existingStudent, student);
                })
                .map(studentRepository::save)
                .orElseThrow(() -> new RecordNotFoundException("Record is not found!!"));
    }

    private Student updateExistingStudent(Student existingStudentRecord, UpdateStudentRequest newStudentRecord) {

        User user = existingStudentRecord.getUser();

        /*
        existingStudentRecord.setRoll(newStudentRecord.getRoll());
        existingStudentRecord.setUserDetails() (newStudentRecord.getFullName());
        existingStudentRecord.setPhoneNumber(newStudentRecord.getPhoneNumber());
        existingStudentRecord.setCourse(newStudentRecord.getCourse());
        existingStudentRecord.setEmail(newStudentRecord.getEmail());
        existingStudentRecord.setSemester(newStudentRecord.getSemester());
        existingStudentRecord.setDob(newStudentRecord.getDob());
        */

        return existingStudentRecord;


    }
}
