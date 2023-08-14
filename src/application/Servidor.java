package application;

import java.io.FileWriter;
import java.io.IOException;

import entity.Veiculo;
import exceptions.SameKeyException;
import exceptions.WrongInsertException;

public class Servidor {

	No<Veiculo> raiz;
	private static int tamanhoArvore = 0;
	private static int contRotacoes = 0;

	public Servidor(No<Veiculo> raiz) {
		this.raiz = raiz;
	}

	public Servidor() {
	}

	public No<Veiculo> inserir(No<Veiculo> next, long id, Veiculo novo) throws SameKeyException {

		if (next == null) {
			writeLog("\tVeículo ID " + id + " adicionado.\n");
			return new No<Veiculo>(id, novo);
		}

		if (next.compareTo(id) == 1) {
			// add esquerda
			next.setEsq(inserir(next.getEsq(), id, novo));
		} else if (next.compareTo(id) == -1) {
			// add direita
			next.setDir(inserir(next.getDir(), id, novo));
		} else {
			throw new SameKeyException(id);
		}

		next.setAlturaNo(1 + maior(altura(next.getEsq()), altura(next.getDir())));
		setTamanhoArvore(next.getAlturaNo());
		
		next = balancear(next);
		
		return next;
	}
	
	public No<Veiculo> alterar(long renavam, Veiculo valor) throws SameKeyException, WrongInsertException{
		No<Veiculo> encontrado = buscar(getRaiz(), renavam);
		if(encontrado != null) {
			
			if(renavam != valor.getRenavam()) {
				remover(getRaiz(), renavam);
				
				No<Veiculo> alterado = inserir(getRaiz(), valor.getRenavam(), valor);
				setRaiz(alterado);
				
				return alterado;
				
			} else {
				encontrado.setValor(valor);
				encontrado.setPkey(valor.getRenavam());
				
				return encontrado;
			}
		} else {
			throw new WrongInsertException();
		}
	}

	public No<Veiculo> remover(No<Veiculo> next, long id) {



		if (next.compareTo(id) == 1) {
			// remover esquerda
			next.setEsq(remover(next.getEsq(), id));
		} else if (next.compareTo(id) == -1) {
			// remover direita
			next.setDir(remover(next.getDir(), id));
		} else if (next.compareTo(id) == 0) {
			next = identificarMetodo(next, id);
			writeLog("Veículo ID " + id + " removido.\n");
		}
		
		if (next == null) {
			return next;
		}

		next.setAlturaNo(1 + maior(altura(next.getEsq()), altura(next.getDir())));
		setTamanhoArvore(next.getAlturaNo());
		
		next = balancear(next);

		return next;
	}
	
	public No<Veiculo> buscar(No<Veiculo> next, long id){
		
		if(next == null) {
			return null;
		} 
		
		if(next.compareTo(id) == 1) {
			return buscar(next.getEsq(), id);
		} else if(next.compareTo(id) == -1) {
			return buscar(next.getDir(), id);
		} else if(next.compareTo(id) == 0) {
			return next;
		} else {
			return null;
		}
	}

	public No<Veiculo> identificarMetodo(No<Veiculo> next, long id) {
		// não tem filhos
		if (next.getEsq() == null && next.getDir() == null) {
			next = null;
		}
		// só tem 1 filho à direita;
		else if (next.getEsq() == null && next.getDir() != null) {
			No<Veiculo> aux = next;
			next = aux.getDir();
			next.setDir(null);
		}
		// só tem 1 filho à esquerda
		else if (next.getEsq() != null && next.getDir() == null) {
			No<Veiculo> aux = next;
			next = aux.getEsq();
			next.setEsq(null);
		}
		// tem 2 filhos
		else if (next.getEsq() != null && next.getDir() != null) {
			No<Veiculo> aux = next.getDir();
			
			while (!(aux.getEsq() == null)) {
				aux = aux.getEsq();
			}

			next.setPkey(aux.getPkey()); // salva o ID
			next.setValor(aux.getValor()); // salva o valor
			aux.setPkey(id); // passa o ID do No a ser excluido

			next.setDir(remover(next.getDir(), id));
		}
		return next;
	}
	
	public No<Veiculo> balancear(No<Veiculo> next) {

		if (next == null) {
			return next;
		}

		int fbNext = obterFB(next);
		int fbSubEsq = obterFB(next.getEsq());
		int fbSubDir = obterFB(next.getDir());

		if (fbNext < -1 && fbSubDir <= 0) {
			writeLog("Rotação Esquerda Simples.\n");
			return res(next);
		}
		if (fbNext > 1 && fbSubEsq >= 0) {
			writeLog("Rotação Direita Simples.\n");
			return rds(next);
		}
		if (fbNext < -1 && fbSubDir > 0) {
			writeLog("Rotação Esquerda Dupla.\n");
			next.setDir(rds(next.getDir()));
			return res(next);
		}
		if (fbNext > 1 && fbSubEsq < 0) {
			writeLog("Rotação Direita Dupla.\n");
			next.setEsq(res(next.getEsq()));
			return rds(next);
		}

		return next;
	}

	public void preOrdem(No<Veiculo> next) {
		if (next != null) {
			System.out.println(next.getPkey() + " ");
			preOrdem(next.getEsq());
			preOrdem(next.getDir());
		}
	}

	public void ordem(No<Veiculo> next) {
		if (next != null) {
			ordem(next.getEsq());
			System.out.println("--------------------------------------------------");
			System.out.println("Placa: " + next.getValor().getPlaca());
			System.out.println("Renavam: " + next.getPkey());
			System.out.println("Condutor: " + next.getValor().getCondutor().getNome()
					+ ", " + next.getValor().getCondutor().getCpf());
			System.out.println("Modelo: " + next.getValor().getModelo());
			System.out.println("Data de Fabricação: " + next.getValor().getFabricacao());
			ordem(next.getDir());
		}
	}

	public void posOrdem(No<Veiculo> next) {
		if (next != null) {
			posOrdem(next.getEsq());
			posOrdem(next.getDir());
			System.out.println(next.getPkey() + " ");
		}
	}

	public int contarNos(No<Veiculo> no) {
		if (no != null) {
			return 1 + contarNos(no.getEsq()) + contarNos(no.getDir());
		}
		return 0;
	}

	public int altura(No<Veiculo> no) {

		if (no == null) {
			return -1;
		} else
			return no.getAlturaNo();
	}

	public int obterFB(No<Veiculo> no) {
		if (no != null) {
			return altura(no.getEsq()) - altura(no.getDir()); // cast porque pkey é long
		} else {
			return 0;
		}
	}

	private int maior(int noA, int noB) {
		return (noA > noB) ? noA : noB;
	}

	public No<Veiculo> res(No<Veiculo> next) {

		No<Veiculo> subDir = next.getDir();
		No<Veiculo> esqSubDir = subDir.getEsq();

		next.setDir(esqSubDir);
		subDir.setEsq(next);
		
		next.setAlturaNo(1 + maior(altura(next.getEsq()), altura(next.getDir())));
		subDir.setAlturaNo(1 + maior(altura(subDir.getEsq()), altura(subDir.getDir())));
		
		contRotacoes++;

		return subDir;
	}

	public No<Veiculo> rds(No<Veiculo> next) {

		No<Veiculo> subEsq = next.getEsq();
		No<Veiculo> dirSubEsq = subEsq.getDir();
		
		next.setEsq(dirSubEsq);
		subEsq.setDir(next);
		
		next.setAlturaNo(1 + maior(altura(next.getEsq()), altura(next.getDir())));
		subEsq.setAlturaNo(1 + maior(altura(subEsq.getEsq()), altura(subEsq.getDir())));
		
		contRotacoes++;

		return subEsq;
	}
	
	public void cleanLog() {
		String path = "src/log.txt";
		
		FileWriter writer;
		try {
			writer = new FileWriter(path, false);
			writer.write("");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeLog(String log) {
		String path = "src/log.txt";
		
		FileWriter writer;
		try {
			writer = new FileWriter(path, true);
			writer.write(log);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public No<Veiculo> getRaiz() {
		return raiz;
	}

	public void setRaiz(No<Veiculo> raiz) {
		this.raiz = raiz;
	}

	public int getTamanhoArvore() {
		return tamanhoArvore;
	}

	public void setTamanhoArvore(int tamanhoArvore) {
		Servidor.tamanhoArvore = tamanhoArvore;
	}

	public int getContRotacoes() {
		return contRotacoes;
	}

	public void setContRotacoes(int contRotacoes) {
		Servidor.contRotacoes = contRotacoes;
	}

}
