package com.felipitogames.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import com.felipitogames.main.Game;

public class Npc extends Entity {

	public String[] frases = new String[2];

	public boolean showMessage = false;

	public Npc(int x, int y, int width, int heigth, BufferedImage sprite) {
		super(x, y, width, heigth, sprite);

	}

	public void tick() {
		int xPlayer = Game.player.getX();
		int yPlayer = Game.player.getY();

		int xNpc = (int) x;
		int yNpc = (int) y;

		if (Math.abs(xPlayer - xNpc) < 40 && Math.abs(yPlayer - yNpc) < 40) {
			int resposta = 0;
			showMessage = true;
			JOptionPane.showMessageDialog(Game.frame,
					"Olá Bob, seja bem vindo. Você fez um bom trabalho com as maletinhas lá fora, mas creio que só isso não seja o suficiente\n"
							+ "para salvar a empresa. Então testarei seu conhecimento em sua área com algumas perguntas:",
					"Claudinha the Boss", JOptionPane.INFORMATION_MESSAGE
			/* poderia ter um icone especial aqui */);
			int randomic = Game.rand.nextInt(5);
			boolean[] ugabuga = { false, false, false, false, false };
			Object[] opts = { "(a) Planejar, organizar, dirigir e controlar",
					"(b) Administrar, planejar, organizar e liderar", "(c) Planejar Administrar, gerir e controlar",
					"(d) Controlar, gerir, planejar e organizar"

			};
			int resp;
			for (int i = 0; i < 2; i++) {
				while (ugabuga[randomic] == true) {
					randomic = Game.rand.nextInt(5);

				}
				ugabuga[randomic] = true;
				switch (randomic) {
				case 0:
					resposta = 0;
					frases[0] = "Quais os pilares da administração";
					frases[1] = "Claudinha the Boss";

					opts[0] = "(a) Planejar, organizar, dirigir e controlar";
					opts[1] = "(b) Administrar, planejar, organizar e liderar";
					opts[2] = "(c) Planejar Administrar, gerir e controlar";
					opts[3] = "(d) Controlar, gerir, planejar e organizar";
					break;
				case 1:
					resposta = 3;
					frases[0] = "O que é a lei da procura e da oferta?";
					frases[1] = "Claudinha the Boss";

					opts[0] = "(a) Espaço onde as empresas oferecem seus produtos";
					opts[1] = "(b) Quando as empresas oferecem produtos ao mercado";
					opts[2] = "(c) Quando os clientes querem comprar produtos";
					opts[3] = "(d) Fenômeno que determina os preços dos produtos";
					break;
				case 2:
					resposta = 2;
					frases[0] = "Qual dessas alternativas corresponde aos 4 p's do marketing?";
					frases[1] = "Claudinha the Boss";

					opts[0] = "(a) Praça, preço, propaganda e promoção";
					opts[1] = "(b) Prioridade, propaganda, preço e promoção";
					opts[2] = "(c) Praça, preço, produto e promoção";
					opts[3] = "(d) Prioridade, praça, produto e programação";
					break;
				case 3:
					resposta = 1;
					frases[0] = "O que é público-alvo?";
					frases[1] = "Claudinha the Boss";

					opts[0] = "(a) O valor médio gasto por cada cliente em geral";
					opts[1] = "(b) Segmento da sociedade com características em comum";
					opts[2] = "(c) Investimentos";
					opts[3] = "(d) Retorno sobre o investimento";
					break;
				case 4:
					resposta = 0;
					frases[0] = "Dentre os produtos indicados abaixo, qual faz parte dos ativos circulantes?";
					frases[1] = "Claudinha the Boss";

					opts[0] = "(a) Estoques, bancos duplicatas e aplicações";
					opts[1] = "(b) Maquinas, equipamentos e prédios";
					opts[2] = "(c) Contas a pagar, empréstimos e impostos";
					opts[3] = "(d) Dívidas com instituições financeiras";
					break;
				}

				resp = JOptionPane.showOptionDialog(Game.frame, frases[0], frases[1], JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, opts, opts[2]);

				if (resp == resposta) {
					if (i == 1) {
						Game.gameState = "WIN";

					} else {
						JOptionPane.showMessageDialog(Game.frame,
								"Muito bem! Agora responda a última pergunta e você salvará a empresa!",
								"Claudinha the Boss", JOptionPane.INFORMATION_MESSAGE);

					}

				} else {
					Game.gameState = "GAME_OVER";
					break;
				}
			}

		} else {
			showMessage = false;
		}
	}

	public void render(Graphics g) {
		super.render(g);

	}

}
