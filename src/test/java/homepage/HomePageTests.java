package homepage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import Util.Funcoes;
import base.BaseTests;
import pages.CarrinhoPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ModalProdutoPage;
import pages.PedidoPage;
import pages.ProdutoPage;

public class HomePageTests extends BaseTests {

	@Test
	public void testContarProdutos_oitoProdutosDiferentes() {
		carregarPaginaInicial();
		assertThat(homePage.contarProdutos(), is(8));
	}
	
	@Test
	public void testValidarCarrinhoZerado_zeroItensNoCarrinho() {
		int produtosNoCarrinho = homePage.obterQuantidadeProdutosNoCarrinho();
		assertThat(produtosNoCarrinho, is(0));
		
	}
	
	ProdutoPage produtoPage;
	String nomeProduto_ProdutoPage;
	@Test
	public void testValidarDetalhesDoProduto_DescricaoEValorIguais() {
		int indice = 0;
		String nomeProduto_HomePage = homePage.obterNomeProduto(indice);
		String precoProduto_HomePage = homePage.obterPrecoProduto(indice);
		
		System.out.println(nomeProduto_HomePage);
		System.out.println(precoProduto_HomePage);
		
		produtoPage = homePage.clicarProduto(indice);
		nomeProduto_ProdutoPage = produtoPage.obterNomeProduto();
		String precoProduto_ProdutoPage = produtoPage.obterPrecoProduto();
		
		System.out.println(nomeProduto_ProdutoPage);
		System.out.println(precoProduto_ProdutoPage);
		
		assertThat(nomeProduto_HomePage.toUpperCase(), is(nomeProduto_ProdutoPage.toUpperCase()));
		assertThat(precoProduto_HomePage, is(precoProduto_ProdutoPage));
		
	}
	
	LoginPage loginPage;
	@Test
	public void testLoginComSucesso_UsuarioLogado() {
		loginPage =  homePage.clicarBotaoSignIn();
		loginPage.preencherEmail("cassia@teste.com");
		loginPage.preencherPassword("123456");
		loginPage.clicarBotaoSignIn();
		assertThat(homePage.estarLogado("Cassia Souza"), is(true));
		carregarPaginaInicial();
	}
	
	@ParameterizedTest
	@CsvFileSource(resources = "/massaTeste_Login.csv", numLinesToSkip = 1, delimiter = ';')
	public void testLogin_UsuarioLogadoComDadosValidos(String nomeTeste, String email, String password, String nomeUsuario, String resultado) {
		
		loginPage =  homePage.clicarBotaoSignIn();
		loginPage.preencherEmail(email);
		loginPage.preencherPassword(password);
		loginPage.clicarBotaoSignIn();
		
		boolean esperado_loginOk;
		if(resultado.equals("positivo"))
			esperado_loginOk = true;
		else
			esperado_loginOk = false;
		
		assertThat(homePage.estarLogado(nomeUsuario), is(esperado_loginOk));
		
		capturarTela(nomeTeste, resultado);
		
		if(esperado_loginOk)
			homePage.clicarBotaoSignOut();
			carregarPaginaInicial();
		
	}
	
	ModalProdutoPage modalProdutoPage;
	@Test
	public void testIncluirProdutoNoCarrinho_ProdutoIncluidoComSucesso() {
		String tamanhoProduto = "M";
		String corProduto = "Black";
		int quantidadeProduto = 2;
		String precoProdutoString;
		Double precoProduto;
		String subtotalString;
		Double subtotal;
		Double subtotalCalculado;
		
		//Pre-condicao: usuario logado
		if(!homePage.estarLogado("Cassia Souza")) {
			testLoginComSucesso_UsuarioLogado();
		}
		//Teste
		//Selecionando produto
		testValidarDetalhesDoProduto_DescricaoEValorIguais();
		//Selecionando o tamanho
		List<String> listaOpcoes = produtoPage.obterOpcoesSelecionadas();
		System.out.println("Opção selecionada é: " + listaOpcoes.get(0));
		System.out.println("Tamanho da lista: " + listaOpcoes.size());
		produtoPage.selecionarOpcaoDropDown(tamanhoProduto);
		
		listaOpcoes = produtoPage.obterOpcoesSelecionadas();
		System.out.println("Opção selecionada é: " + listaOpcoes.get(0));
		System.out.println("Tamanho da lista: " + listaOpcoes.size());
		
		//Selecionando cor
		produtoPage.selecionarCorPreta();		
		//Selecionando quantidade
		produtoPage.alterarQuantidade(quantidadeProduto);
		//Adicionando no carrinho
		modalProdutoPage = produtoPage.clicarBotaoAddToCart();
		
		//Validacoes
		assertTrue(modalProdutoPage.obterMensagemProdutoAdicionado().endsWith("Product successfully added to your shopping cart"));
		
		precoProdutoString = modalProdutoPage.obterPrecoProduto();
		precoProdutoString = precoProdutoString.replace("$", "");
		precoProduto = Double.parseDouble(precoProdutoString);
		
		subtotalString = modalProdutoPage.obterSubtotal();
		subtotalString = subtotalString.replace("$", "");
		subtotal = Double.parseDouble(subtotalString);
		subtotalCalculado = quantidadeProduto * precoProduto;
		
		assertThat(modalProdutoPage.obterTamanhoProduto(), is(tamanhoProduto));
		assertThat(modalProdutoPage.obterCorProduto(), is(corProduto));
		assertThat(modalProdutoPage.obterQuantidadeProduto(), is(Integer.toString(quantidadeProduto)));
		assertThat(subtotal, is(subtotalCalculado));
		assertThat(modalProdutoPage.obterDescricaoProduto().toUpperCase(), is(nomeProduto_ProdutoPage.toUpperCase()));
		
	}
	
	//Valores esperados
	String esperado_nomeProduto = "Hummingbird printed t-shirt";
	Double esperado_precoProduto = 19.12;
	String esperado_tamanhoProduto = "M";
	String esperado_corProduto = "Black";
	int esperado_input_quantidadeProduto = 2;
	Double esperado_subtotalProduto = esperado_precoProduto * esperado_input_quantidadeProduto;
	
	int esperado_numeroItensTotal = esperado_input_quantidadeProduto;
	Double esperado_subtotalTotal = esperado_subtotalProduto;
	Double esperado_shippingTotal = 7.0;
	Double esperado_totalTaxaExclTotal = esperado_subtotalTotal + esperado_shippingTotal;
	Double esperado_totalTaxaIncTotal = esperado_totalTaxaExclTotal;
	Double esperado_taxesTotal = 0.00;
	
	String esperado_nomeCliente = "Cassia Souza";
	
	CarrinhoPage carrinhoPage;
	@Test
	public void testIrParaCarrinho_InformacoesPersistidas() {
		//Pre-condicoes:
		//Produto incluido na tela ModalProdutoPage
		testIncluirProdutoNoCarrinho_ProdutoIncluidoComSucesso();
		
		carrinhoPage = modalProdutoPage.clicarBotaoProceedToCheckout();
		//Teste
		//Validar todos os elementos da tela
		System.out.println("*** TELA DO CARRINHO ***");
		System.out.println(carrinhoPage.obter_nomeProduto());
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_precoProduto()));
		System.out.println(carrinhoPage.obter_tamanhoProduto());
		System.out.println(carrinhoPage.obter_corProduto());
		System.out.println(Funcoes.removeTextoItensDevolveInt(carrinhoPage.obter_input_quantidadeProduto()));
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subtotalProduto()));
		
		System.out.println("*** ITENS DE TOTAL ***");
		System.out.println(Funcoes.removeTextoItensDevolveInt(carrinhoPage.obter_numeroItensTotal()));
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subtotalTotal()));
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_shippingTotal()));
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_totalTaxaExclTotal()));
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_totalTaxaIncTotal()));
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_taxesTotal()));
		
		//Assercoes Hamcrest
		//Tela do carrinho
		assertThat(carrinhoPage.obter_nomeProduto(), is(esperado_nomeProduto));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_precoProduto()), is(esperado_precoProduto));
		assertThat(carrinhoPage.obter_tamanhoProduto(), is(esperado_tamanhoProduto));
		assertThat(carrinhoPage.obter_corProduto(), is(esperado_corProduto));
		assertThat(Funcoes.removeTextoItensDevolveInt(carrinhoPage.obter_input_quantidadeProduto()), is(esperado_input_quantidadeProduto));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subtotalProduto()), is(esperado_subtotalProduto));
		//Itens de total
		assertThat(Funcoes.removeTextoItensDevolveInt(carrinhoPage.obter_numeroItensTotal()), is(esperado_numeroItensTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subtotalTotal()), is(esperado_subtotalTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_shippingTotal()), is(esperado_shippingTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_totalTaxaExclTotal()), is(esperado_totalTaxaExclTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_totalTaxaIncTotal()), is(esperado_totalTaxaIncTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_taxesTotal()), is(esperado_taxesTotal));
		
	}
	
	CheckoutPage checkoutPage;
	@Test
	public void testIrParaCheckout_FreteMeioPagamentoEnderecoListadosOk() {
		//Pre - condicoes
		//Produto dispon�vel no carrinho de compras
		testIrParaCarrinho_InformacoesPersistidas();
		
		//Testes		
		//Clicar no bot�o Proceed to checkout
		checkoutPage = carrinhoPage.clicarBotaoProceedToCheckout();
		
		//Validar informacoes na tela
		assertThat(Funcoes.removeCifraoDevolveDouble(checkoutPage.obter_totalTaxIncTotal()), is(esperado_totalTaxaIncTotal));
		assertTrue(checkoutPage.obter_nomeCliente().startsWith(esperado_nomeCliente));
		
		checkoutPage.clicarBotaoContinueAddress();
		
		String encontrado_shippingValor = checkoutPage.obter_shippingValor();
		encontrado_shippingValor = Funcoes.removeTexto(encontrado_shippingValor, " tax excl.");
		Double encontrado_shippingValor_Double = Funcoes.removeCifraoDevolveDouble(encontrado_shippingValor); 
		assertThat(encontrado_shippingValor_Double, is(esperado_shippingTotal));
		
		checkoutPage.clicarBotaoContinueShipping();
		
		//Selecionar opcao Pay by Check
		checkoutPage.selecionarRadioPayByCheck();
		
		//Validar valor do cheque (Amount)
		String encontrado_amountPayByCheck = checkoutPage.obter_amountPayByCheck();
		encontrado_amountPayByCheck = Funcoes.removeTexto(encontrado_amountPayByCheck, " (tax incl.)");
		Double encontrado_amountPayByCheck_Double = Funcoes.removeCifraoDevolveDouble(encontrado_amountPayByCheck);
		assertThat(encontrado_amountPayByCheck_Double, is(esperado_totalTaxaIncTotal));
		
		//Clicar na opcao I agree
		checkoutPage.selecionar_checkboxIAgree();
		
		assertTrue(checkoutPage.estaSelecionadoCheckboxIAgree());
		
	}
	
	PedidoPage pedidoPage;
	@Test
	public void testFinalizarPedido_pedidoFinalizadoComSucesso() {
		//Pre - condicoes: checkout concluido
		testIrParaCheckout_FreteMeioPagamentoEnderecoListadosOk();
		
		//Teste
		//Cicar no botao confirmar pedido
		pedidoPage = checkoutPage.clicarBotaoConfirmaPedido();
		//Validar valores da tela
		assertTrue(pedidoPage.obter_textoPedidoConfirmado().endsWith("YOUR ORDER IS CONFIRMED"));
		assertThat(pedidoPage.obter_email(), is("cassia@teste.com"));
		assertThat(pedidoPage.obter_totalProdutos(), is(esperado_subtotalTotal));
		assertThat(pedidoPage.obter_totalTaxIncl(), is(esperado_totalTaxaIncTotal));
		assertThat(pedidoPage.obter_metodoPagamento(), is("check"));
		
	}
	
		
	
}
