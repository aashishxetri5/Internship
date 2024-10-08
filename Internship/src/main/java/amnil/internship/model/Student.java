package amnil.internship.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student {

    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "user_details_id", referencedColumnName = "id")
    private User user;

    @Column(unique = true, nullable = false)
    private Long roll;

    private int semester;
    private LocalDate dob;

    private String course;

    public Student(User user, Long roll, int semester, LocalDate dob, String course) {
        this.user = user;
        this.roll = roll;
        this.semester = semester;
        this.dob = dob;
        this.course = course;
    }

    // Calculates age from given DOB
    public int getAge() {
        return (LocalDate.now().getYear() - this.getDob().getYear());
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", userDetails=" + user +
                ", roll=" + roll +
                ", semester=" + semester +
                ", dob=" + dob +
                ", courses=" + course +
                '}';
    }
}
