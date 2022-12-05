package src;
import java.util.Calendar;

public class Produto{
    private int quantidade;
    private String nome;
    private Double custo;
    private Double preco_de_venda;
    private Calendar validade;
    private Double margem_de_lucro;

    public Double getMargem_de_lucro() {
        return margem_de_lucro;
    }

    
    public void setMargem_de_lucro(Double margem_de_lucro) {
        this.margem_de_lucro = margem_de_lucro;
    }

    public int getQuantidade() {
        return quantidade;
    }

    
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    
    public String getNome() {
        return nome;
    }

   
    public void setNome(String nome) {
        this.nome = nome;
    }

    
    public Double getCusto() {
        return custo;
    }

   
    public void setCusto(Double custo) {
        this.custo = custo;
    }

   
    public Double getPreco_de_venda() {
        return preco_de_venda;
    }

   
    public void setPreco_de_venda(Double preco_de_venda) {
        this.preco_de_venda = preco_de_venda;
    }

   
    public void setValidade(String validade) {
        this.validade = Calendar.getInstance();
        int ano, mes, dia;
        String datas[] = validade.split("/");
        ano = Integer.parseInt(datas[2]);
        mes = Integer.parseInt(datas[1]);
        dia = Integer.parseInt(datas[0]);

        this.validade.set(ano,mes,dia);
    }

    public void setvalidade(Calendar validade) {
        this.validade = validade;
    }

    public String getValidade(){
        String retorno_validade = ""+validade.get(Calendar.DAY_OF_MONTH) + "/" + (validade.get(Calendar.MONTH)) + "/" + validade.get(Calendar.YEAR);
        return retorno_validade;
    }

    @Override
    public String toString() {

        return "Nome: " + nome + " Custo: " + custo + " Preço de Venda: " + preco_de_venda + " Validade: " + this.getValidade() + " Quantidade: " + quantidade + " Margem de lucro: " + margem_de_lucro + "%";
    }

}
