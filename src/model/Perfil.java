package model;


public enum Perfil {

    GERENCIA("Gerência", "gerente123"),
    FUNCIONARIO("Funcionário", "func123");

    private final String descricao;
    private final String senha;

    Perfil(String descricao, String senha) {
        this.descricao = descricao;
        this.senha = senha;
    }

    public boolean senhaCorreta(String tentativa) {
        return senha.equals(tentativa);
    }

    
    public String toString() {
        return descricao;
    }
}