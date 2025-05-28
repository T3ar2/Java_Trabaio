import java.io.*;
import java.util.Scanner;

public class Menu {
    public void MontarMenu() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("Menu1.txt"))) {
            String linha;
            while ((linha = bufferedReader.readLine()) != null) {
                String[] campos = linha.split(";");
                if (campos.length == 4 && campos[0].trim().equals("1")) {
                    System.out.println(campos[2].trim() + " - " + campos[3].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler Menu1.txt: " + e.getMessage());
        }
    }

    public void MontarMenu(int nivel, int opcaoPai) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("Menu1.txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] campos = line.split(";");
                if (campos.length == 4 &&
                        Integer.parseInt(campos[0].trim()) == nivel &&
                        Integer.parseInt(campos[1].trim()) == opcaoPai) {
                    System.out.println(campos[2].trim() + " - " + campos[3].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Erro em ler  o Menu1.txt: " + e.getMessage());
        }
    }

    public int OpcaoEscolhida(int posicao_inicial, int posicao_final, String mensagem) {
        Scanner scanner_chan = new Scanner(System.in);
        int opcao;
        while (true) {
            System.out.print(mensagem + " (entre " + posicao_inicial + " e " + posicao_final + "): ");
            if (scanner_chan.hasNextInt()) {
                opcao = scanner_chan.nextInt();
                if (opcao >= posicao_inicial && opcao <= posicao_final) {
                    break;
                } else {
                    System.out.println("Erro: Opção inválida. Por favor, digite um valor entre " + posicao_inicial + " e " + posicao_final + ".");
                }
            } else {
                System.out.println("Erro: Entrada inválida. Por favor, digite um número inteiro.");
                scanner_chan.next();
            }
        }
        return opcao;
    }

}
