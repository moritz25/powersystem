package se.kth.eh2745.moritzv.assigment2.object;

import java.util.Arrays;
import java.util.List;

public enum SystemClasses {
	UNKOWN("unkown",new Integer[] {}),
	HIGH_LOAD("High load rate during peak hours",new Integer[] {3, 5, 6, 8, 13, 27, 28, 30, 31, 35, 39, 43, 46, 48, 51, 60, 66, 69, 70, 72, 78, 79, 89, 93, 95, 105, 110, 111, 112, 118, 122, 130, 131, 134, 137, 139, 143, 145, 155, 157, 158, 169, 176, 187, 188, 189, 190, 199, 200}),
	GEN_SHUT("Shut down of generator for maintenance",new Integer[] {4, 11, 12, 14, 18, 20, 22, 25, 29, 33, 38, 40, 55, 56, 57, 61, 62, 63, 75, 76, 84, 85, 86, 91, 100, 102, 104, 106, 109, 114, 116, 121, 126, 128, 132, 138, 140, 142, 147, 152, 154, 156, 163, 167, 168, 173, 177, 184, 186, 194, 196, 197, 198}),
	LOW_LOAD("Low load rate during night",new Integer[] {1, 2, 7, 10, 15, 16, 19, 21, 36, 37, 42, 44, 49, 52, 54, 58, 64, 67, 68, 71, 74, 80, 83, 88, 92, 94, 96, 97, 99, 107, 115, 120, 123, 124, 125, 127, 129, 133, 135, 141, 144, 149, 160, 161, 164, 166, 174, 175, 191, 192, 193}),
	LINE_DIS("Disconnection of a line for maintenance",new Integer[] {9, 17, 23, 24, 26, 32, 34, 41, 45, 47, 50, 53, 59, 65, 73, 77, 81, 82, 87, 90, 98, 101, 103, 108, 113, 117, 119, 136, 146, 148, 150, 151, 153, 159, 162, 165, 170, 171, 172, 178, 179, 180, 181, 182, 183, 185, 195});
	
	private String longname;
	private Integer[] autoTimes;

	private SystemClasses(String longname,Integer[] autoTimes) {
		this.longname = longname;
		this.autoTimes=autoTimes;
	}

	
	public String toString(){
		return longname;
	}
	public List<Integer> getAutoTimes(){
		return Arrays.asList(autoTimes);
	}
	
	
}
