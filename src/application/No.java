package application;

// Nó para Árvore AVL
public class No<T> implements Comparable<Long> {

	private long pkey;
	private T valor;
	private int alturaNo;
	private No<T> esq, dir;
	
	public No(long pkey, T valor) {
		this.pkey = pkey;
		this.valor = valor;
		this.esq = null;
		this.dir = null;
	}
	
	@Override
	public int compareTo(Long anotherKey) {
		// TODO Auto-generated method stub
		
		if(this.getPkey() < anotherKey) {
			return -1;
		} else if(this.getPkey() > anotherKey) {
			return 1;
		}
		
		return 0;
	}
	
	public long getPkey() {
		return pkey;
	}
	public void setPkey(long pkey) {
		this.pkey = pkey;
	}
	public T getValor() {
		return valor;
	}
	public void setValor(T valor) {
		this.valor = valor;
	}
	public int getAlturaNo() {
		return alturaNo;
	}
	public void setAlturaNo(int alturaNo) {
		this.alturaNo = alturaNo;
	}
	public No<T> getEsq() {
		return esq;
	}
	public void setEsq(No<T> esq) {
		this.esq = esq;
	}
	public No<T> getDir() {
		return dir;
	}
	public void setDir(No<T> dir) {
		this.dir = dir;
	}
	
}
