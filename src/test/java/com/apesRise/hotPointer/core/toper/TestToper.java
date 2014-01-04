package com.apesRise.hotPointer.core.toper;

public class TestToper {
	static class GG implements MaxTopInterface<Integer> {
		int v = 0;

		public GG(int vv) {
			v = vv;
		}

		@Override
		public Integer getTime() {
			// TODO Auto-generated method stub
			return new Integer(v);
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return v + "";
		}

		@Override
		public String getID() {
			return v + "";
		}

	}

	public static void main(String[] a) {

		MaxTopo<GG> top = new MaxTopo<GG>(3);
		top.push(new GG(1));
		top.push(new GG(2));
		top.push(new GG(0));
		top.push(new GG(3));
		top.push(new GG(-1));
		top.push(new GG(10));
		top.push(new GG(9));
		top.push(new GG(5));
		top.push(new GG(2));

		for (GG gg : top.getResult()) {
			System.out.println(gg.v);
		}

	}
}
