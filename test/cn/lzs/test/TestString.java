package cn.lzs.test;

public class TestString {
	public static void main(String[] args) {
		String a="我是一个人<br#>y<br#>n<br#>1<br#><br#>我爱她<br#>一定<br#>可能<br#>2<br#><br#><br#><br#><br#><br#><br#><br#>";
		System.out.println(a.split("<br#>").length);
	}
}
