package main;

/**
 * Created by HelioCaetano on 27-03-2014.
 */
public class Pessoa {
    private int id;
    protected int idade;
    public String nome;

    public Pessoa(){}

    public Pessoa(int id, int idade, String nome){
        this.id = id;
        this.idade = idade;
        this.nome = nome;
    }

    public void setDummy(){}

    public void setTheDummy(int a, String s){}

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setIdade(int idade){
        this.idade = idade;
    }

    public int getId(){
        return id;
    }

    public int getIdade() {
        return idade;
    }
}
