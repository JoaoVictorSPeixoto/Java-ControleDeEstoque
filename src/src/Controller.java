package src;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Controller {
    private Janela janela;
    private static Controller acessaCSV;

    private Controller(){}

    public static Controller istancia(){
        if(acessaCSV == null){
            acessaCSV = new Controller();
        }
        return acessaCSV;
    }

    public String geraStringRetorno(Produto produto){
        return produto.getNome()+";"+produto.getQuantidade()+";"+produto.getValidade()+";"+produto.getCusto()+";"+produto.getPreco_de_venda()+";"+produto.getMargem_de_lucro()+";";
    }

    public void salvar(Produto produto) throws Controller.CampoNomeInvalidoException, Controller.CampoQuantidadeInvalidoException, Controller.CampoValidadeInvalidoException, Controller.CampoCustoInvalidoException, Controller.CampoMargemDeLucroInvalidoException{
        switch(produto.getNome()){
            case "":
                throw new  CampoNomeInvalidoException();
            case " ":
                throw new CampoNomeInvalidoException();
            case "  ":
                throw new CampoNomeInvalidoException();
            case "Nome":
                throw new CampoNomeInvalidoException();
        }

        if(produto.getQuantidade() <= 0){
            throw new CampoQuantidadeInvalidoException();
        }

        if(produto.getCusto() <=  0){
            throw new CampoCustoInvalidoException();
        }

        if(produto.getMargem_de_lucro() <=  0){
            throw new CampoMargemDeLucroInvalidoException();
        }

        if(produto.getValidade().equals("31/11/2")){
            throw new CampoValidadeInvalidoException();
        }
        try {
            File file = new File("src\\BD.csv");
            FileWriter fw = new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(geraStringRetorno(produto));
            bw.newLine();
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.getMessage();
        }
        
    }

    public void deletar(String produto_nome){
        try {
            File file = new File("src\\BD.csv");
            Scanner scanner = new Scanner(file);
            ArrayList<String> linhas = new ArrayList<String>();
            while(scanner.hasNext()){
                String linha;
                linha = scanner.nextLine();
                linhas.add(linha);
                linha="";
            }
            scanner.close();
            for (int i = 0; i < linhas.size(); i++) {
                String[] palavras = linhas.get(i).split(";");
                if(palavras[0].equals(produto_nome)){
                    linhas.remove(i);
                }
            }

            
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < linhas.size(); i++) {
                bw.write(linhas.get(i));
                bw.newLine();
            }
            
            bw.close();
            fw.close();
            janela.preencheCorpoPaginaEstoque(getProdutos(),true);

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public ArrayList<Produto> getProdutos(){
        ArrayList<Produto> produtos = new ArrayList<>();
        File file = new File("src\\BD.csv");
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            ArrayList<String> linhas = new ArrayList<String>();
            scanner.nextLine();
            while(scanner.hasNext()){
                String linha;
                linha = scanner.nextLine();
                linhas.add(linha);
                linha="";
            }
            
            for (int i = 0; i < linhas.size(); i++) {
                String[] atributos = linhas.get(i).split(";");
                Produto produto = new Produto();
                produto.setNome(atributos[0]);
                produto.setQuantidade(Integer.parseInt(atributos[1]));
                produto.setValidade(atributos[2]);
                produto.setCusto(Double.parseDouble(atributos[3]));
                produto.setPreco_de_venda(Double.parseDouble(atributos[4]));
                produto.setMargem_de_lucro(Double.parseDouble(atributos[5]));
                produtos.add(produto);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
            
        return produtos;
    }

    public void Atualizar(Produto produto){
        deletar(produto.getNome());
            try {
                salvar(produto);
                janela.preencheCorpoPaginaEstoque(getProdutos(),false);
            } catch (Controller.CampoNomeInvalidoException | Controller.CampoQuantidadeInvalidoException
                    | Controller.CampoValidadeInvalidoException | Controller.CampoCustoInvalidoException
                    | Controller.CampoMargemDeLucroInvalidoException e) {
               e.getMessage();
            }
    }

    public void RemoveQuantidade(int quantidade, String nome) throws Controller.NomeinvalidoExecption, Controller.NomeNaoEncontradoException, Controller.QuantidadeMaiorQueONumeroEmEstoqueException, Controller.QuantidadeMenorQue0Exception{

        boolean nomeEcntonrado = false;
        if(quantidade <= 0){
            throw new QuantidadeMenorQue0Exception();
        }
        switch(nome){
            case "Nome":
                throw new NomeinvalidoExecption();
                
            case "":
                throw new NomeinvalidoExecption();

            case " ":
                throw new NomeinvalidoExecption();

        }

        ArrayList<Produto> produtos = new ArrayList<>();
        produtos = getProdutos();
        Produto produtoNovo = new Produto();
        for(int i = 0; i < produtos.size(); i++) {
            if(produtos.get(i).getNome().equals(nome)){
                if(produtos.get(i).getQuantidade() < quantidade){
                    throw new QuantidadeMaiorQueONumeroEmEstoqueException();
                }
                int quantidadeNova = produtos.get(i).getQuantidade() - quantidade;
                produtos.get(i).setQuantidade(quantidadeNova);
                produtoNovo = produtos.get(i);
                nomeEcntonrado = true;
            }
        }

        if(nomeEcntonrado != true){
            throw new NomeNaoEncontradoException();
        }

        if(produtoNovo.getQuantidade() == 0){
            Object[] opcoes = {"SIM","NÃO"};
            int resposta = JOptionPane.showOptionDialog(null,"Você deseja remover "+quantidade+" unidade(s) do produto "+ nome+"?", "Alerta!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[1]);

            if(resposta == JOptionPane.NO_OPTION || resposta  == JOptionPane.CLOSED_OPTION){
                
            }else{
                JOptionPane.showMessageDialog(null, quantidade+" unidade(s) do produto "+nome+" removida(s)!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                deletar(nome);
            }
        }else{
            try {
                deletar(nome);
                salvar(produtoNovo);
            } catch (Controller.CampoNomeInvalidoException | Controller.CampoQuantidadeInvalidoException
                    | Controller.CampoValidadeInvalidoException | Controller.CampoCustoInvalidoException
                    | Controller.CampoMargemDeLucroInvalidoException e) {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Quantidade Modificada do produto: " + nome, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        
        

    }

    public void run(){
        janela = Janela.instaciaJanelaMain("CidaMaisVocê", getProdutos());
    }

    public Janela getJanela(){
        return janela;
    }

    public class NomeinvalidoExecption extends Exception{
        @Override
        public String getMessage(){
            return "Nome Invalido";
        }
    }

    public class NomeNaoEncontradoException extends Exception{
        @Override
        public String getMessage(){
            return "Nome não encontrado";
        }
    }

    public class QuantidadeMaiorQueONumeroEmEstoqueException extends Exception{
        @Override
        public String getMessage(){
            return "Quantidade Inregular";
        }
    }
    public class QuantidadeMenorQue0Exception extends Exception{
        @Override
        public String getMessage(){
            return "Quantidade não pode ser menor que 0";
        }
    }
    
    public class CamposFormulariosNaoPreenchidosException extends Exception{
        @Override
        public String getMessage(){
            return "Nenhum campo foi preenchido!";
        }
    }

    public class CampoNomeInvalidoException extends Exception{
        @Override
        public String getMessage(){
            return "Nome invalido!";
        }
    }

    public class CampoQuantidadeInvalidoException extends Exception{
        @Override
        public String getMessage(){
            return "Quantidade invalida!";
        }
    }

    public class CampoValidadeInvalidoException extends Exception{
        @Override
        public String getMessage(){
            return "Validade invalida!";
        }
    }

    public class CampoCustoInvalidoException extends Exception{
        @Override
        public String getMessage(){
            return "Cuto Invalido!";
        }
    }

    public class CampoMargemDeLucroInvalidoException extends Exception{
        @Override
        public String getMessage(){
            return "Marge de Lucro Invalida!";
        }
    }
}
