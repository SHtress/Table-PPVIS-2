package model;

import java.util.List;

public class Student {
	private SNP snp;
	private String 	   group;
	private List<Exam> examList;

	public Student(SNP snp, String group, List<Exam> examList){
		this.snp      = snp;
		this.group    = group;
		this.examList = examList;
	}

	public SNP getSnp(){
		return snp;
	}

	public String getSurname(){
		return snp.getSurname();
	}

	public void setSnp(SNP snp){
		this.snp = snp;
	}

	public String getAlignSnp(){
		return snp.getSurname() + " " + snp.getName() + " " + snp.getPatronym();
	}

	public void setAlignSnp(String alignSnp){
		this.snp = new SNP(alignSnp);
	}

	public String getGroup(){
		return group;
	}

	public void setGroup(String group){
		this.group = group;
	}

	public List<Exam> getExamList(){
		return examList;
	}

	public void setExamList(List<Exam> examList){
		this.examList = examList;
	}

	//public String getExamName(int i){
	//	return examList.get(i).getName();
	//}

	public int getExamScore(int i){
		return examList.get(i).getScore();
	}
}
