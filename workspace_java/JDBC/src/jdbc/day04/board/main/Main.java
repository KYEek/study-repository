package jdbc.day04.board.main;


import java.util.Scanner;

import jdbc.day04.member.controller.MemberController;


public class Main {

	public static void main(String[] args) {
		MemberController ctrl = new MemberController();
		
		Scanner sc = new Scanner(System.in);
		
		ctrl.menu_Start(sc);
		
		sc.close();
		System.out.println("\n (o゜▽゜)o☆  프로그램 종료  o(*￣▽￣*)ブ");

	}

}
