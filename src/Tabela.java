import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Tabela {

	public static void main(String[] args) throws SQLException {
		Scanner entrada = new Scanner(System.in);

		System.out.println("Todas Databases criadas");
		System.out.println();
		String listarDatabasesSql = "SHOW DATABASES";

		Connection conexao = FabricaConexao.getConexao();

		Statement stmt = conexao.createStatement();

		ResultSet result = stmt.executeQuery(listarDatabasesSql);

		while (result.next()) {

			String dataBaseName = result.getString(1);

			System.out.println(dataBaseName);

		}

		System.out.println();
		System.out.println("\nDigite o nome da base de dados na qual deseja executar as operações:");
		String escolhaDatabase = entrada.next();

		System.out.println();
		String listarTabelasSql = "SHOW TABLES FROM " + escolhaDatabase;

		System.out.print("As tabelas da base de dados '" + escolhaDatabase + "' são: ");
		System.out.println();
		result = stmt.executeQuery(listarTabelasSql);

		while (result.next()) {

			String TabelaName = result.getString(1);
			System.out.println("'" + TabelaName + "'");

		}

		System.out.println();
		System.out.println("Voce selecionou a base de dados " + escolhaDatabase);
		System.out.println("Selecione qual tabela deseja executar as operações:");
		String escolhaTabela = entrada.next();

		java.sql.DatabaseMetaData metaData = conexao.getMetaData();
		ResultSet resul = metaData.getTables(null, null, escolhaTabela, null);

		if (resul.next()) {

			boolean opcoes = false;
			while (!opcoes) {
				System.out.println();
				System.out
						.println("Selecione a opção na qual voce deseja executar na tabela '" + escolhaDatabase + "'");
				System.out.println();
				System.out.println("(1 = Insert)");
				System.out.println("(2 = Delete)");
				System.out.println("(3 = Create table)");
				System.out.println("(4 = Create columns)");
				System.out.println("(5 = Create Database)");
				System.out.println("(6 = Sair)");
				System.out.println();
				System.out.println("Digite sua opção: ");
				int escolha = entrada.nextInt();

				switch (escolha) {

				case 1:
					inserirSQL(entrada);
					break;

				case 2:
					deleteSQL(entrada);
					break;

				case 3:
					createTableSQL(entrada);

					break;

				case 4:
					createColumnSQL(entrada);
					break;
				case 5:
					createDatabaseSQL(entrada);
					break;
				case 6:
					System.out.println("Fim !");
					System.exit(escolha);

					break;
				default:
					System.err.println("Entrada inválida");
					break;

				}
			}
			
		} else {
			System.out.println("A tabela '" + escolhaTabela + "' não existe.");
		}
		
		conexao.close();
		entrada.close();
		
	}

	static void inserirSQL(Scanner entrada) throws SQLException {
		try {
			String insertSQL = "INSERT INTO pessoas(nome) VALUES (?)";

			Connection conexao = FabricaConexao.getConexao();
			PreparedStatement stmt = conexao.prepareStatement(insertSQL);

			System.out.println("Digite o nome no qual deseja inserir na tabela pessoas");
			String nome = entrada.next();

			if (nome != null) {

				stmt.setString(1, nome);
				stmt.execute();
				System.out.println("Inserido com sucesso");

			} else {
				System.out.println("Não foi possível inserir");
			}
		} catch (SQLException e) {

			System.out.println(e.getMessage());
		}
	}

	static void deleteSQL(Scanner entrada) throws SQLException {
		try {
			String deleteSQL = "DELETE FROM pessoas WHERE nome = ?";

			Connection conexao = FabricaConexao.getConexao();
			PreparedStatement stmt = conexao.prepareStatement(deleteSQL);

			System.out.println("Oque voce deseja deletar ?: ");
			String escolha = entrada.next();

			if (escolha != null) {

				if ("coluna".equalsIgnoreCase(escolha)) {

					System.out.println("Oque voce deseja deletar ?");
					String escolhaDelete = entrada.next();

					if ("nome".equalsIgnoreCase(escolhaDelete)) {

						System.out.println("Qual nome voce deseja deletar ?");
						String nomeDelete = entrada.next();

						deleteSQL = "DELETE FROM pessoas WHERE codigo = " + nomeDelete;
						stmt.executeUpdate(deleteSQL);
						System.out.println("Deletado com sucesso");

					} else if ("id".equalsIgnoreCase(escolhaDelete)) {

						System.out.println("Qual id voce deseja deletar ?");
						String codigoDelete = entrada.next();

						deleteSQL = "DELETE FROM pessoas WHERE id = " + codigoDelete;
						stmt.executeUpdate(deleteSQL);
						System.out.println("Deletado com sucesso");

					} else if ("coluna".equalsIgnoreCase(escolhaDelete)) {

						System.out.println("Qual coluna voce deseja deletar ?");
						String codigoDelete = entrada.next();

						deleteSQL = "ALTER TABLE pessoas DROP COLUMN " + codigoDelete;
						stmt.executeUpdate(deleteSQL);
						System.out.println("Deletado com sucesso");

					}

				} else if ("tabela".equalsIgnoreCase(escolha)) {

					System.out.println("Qual o nome da tabela que deseja deletar ?");
					String nomeTabela = entrada.next();

					deleteSQL = "DROP TABLE " + nomeTabela;
					stmt.execute(deleteSQL);
					System.out.println("Deletado com sucesso");

				} else if ("database".equalsIgnoreCase(escolha)) {

					System.out.println("Qual o nome da database que deseja deletar ?");
					String nomeDatabase = entrada.next();
					deleteSQL = "DROP DATABASE " + nomeDatabase + " ";
					stmt.executeUpdate(deleteSQL);
					stmt.setString(1, nomeDatabase);
					System.out.println("Deletado com sucesso !");

				}

			} else {

				System.out.println(" Não foi possível deletar !");

			}

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}
	}

	static void createTableSQL(Scanner entrada) throws SQLException {
		try {
			String sql = " ";
			Connection conexao = FabricaConexao.getConexao();
			Statement stmt = conexao.createStatement();

			System.out.println("Digite o nome da tabela na qual deseja criar");
			String nomeTabela = entrada.next();

			System.out.println("Ok, agora adicione uma coluna para sua tabela ");
			String coluna = entrada.next();

			if (nomeTabela != null) {
				sql = "CREATE TABLE IF NOT EXISTS " + nomeTabela + " (id int auto_increment primary KEY, " + coluna
						+ " VARCHAR(80) NOT NULL)";
				stmt.executeUpdate(sql);
				System.out.println("Tabela criada com sucesso !");

			} else {
				System.out.println("Não foi possível criar a Tabela");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	static void createColumnSQL(Scanner entrada) throws SQLException {

		try {

			String sql = " ";
			Connection conexao = FabricaConexao.getConexao();
			Statement stmt = conexao.createStatement();

			System.out.println("Digite a coluna na qual deseja inserir na tabela pessoas");
			String nomeColuna = entrada.next();

			if (nomeColuna != null) {
				sql = "ALTER TABLE pessoas ADD COLUMN " + nomeColuna + " VARCHAR(100)";

				stmt.executeUpdate(sql);
				System.out.println("Coluna criada com sucesso !");

			} else {

				System.out.println("Não foi possível criar a coluna, valor null");
			}

		} catch (SQLException e) {

			System.out.println(e.getMessage());
		}
	}

	static void createDatabaseSQL(Scanner entrada) throws SQLException {
		try {
			String sql = "";
			Connection conexao = FabricaConexao.getConexao();
			Statement stmt = conexao.createStatement();

			System.out.println("Digite o nome da sua Database");
			String nomeDatabase = entrada.next();

			if (nomeDatabase != null) {

				sql = "CREATE DATABASE " + nomeDatabase;
				stmt.executeUpdate(sql);
				stmt.close();
				System.out.println("Database criada com sucesso");
			} else {
				System.out.println("Não foi possivel criar o Database");
			}
		} catch (SQLException e) {

			System.out.println(e.getMessage());
		}

	}

}