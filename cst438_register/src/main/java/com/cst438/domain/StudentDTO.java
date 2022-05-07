package com.cst438.domain;

public class StudentDTO {
	public int student_id;
	public String name;
	public String email;
	
	@Override
	public String toString() {
		return "StudentDTO [student_id=" + student_id + "name=" + name + "email" + email + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentDTO other = (StudentDTO) obj;
		if (student_id != other.student_id)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}