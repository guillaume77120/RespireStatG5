package vue;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import controleur.ConvertCSV;
import controleur.StatEtab;
import model.Etablissement;

public class FenetrePrincipale extends JFrame{
	private static final long serialVersionUID = 1L;
	private String csvPath;
	private static String csvFileName = "ecoles-creches-idf.csv";
	public FenetrePrincipale(){
		super();
		build();
	}

	private void build(){
		File f;
		String message="";
        do {
        	//csvPath = JOptionPane.showInputDialog( "Entrez le chemin vers le fichier : "+csvFileName );
        	csvPath = "C:\\Users\\gbout\\eclipse-workspace\\RespireStatEtu";
			f = new File(csvPath+"/"+csvFileName);
			if(!f.exists())
				message = "Le fichier n'a pas été trouvé.";
			else
				message = "Le fichier a été trouvé.";
			JOptionPane.showMessageDialog(null, message+"\n"+csvPath+"/"+csvFileName);	
        }while(!f.exists());
		ConvertCSV.chargerEtablissements(csvPath+"/"+csvFileName);
		
		setTitle("RespireStat");
		setSize(1020,1020);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(buildContentPaneHomePage());
	}
	private JPanel buildContentPaneHomePage(){
		System.out.println("Build content pane");
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		//ONGLETS
		JTabbedPane onglets = new JTabbedPane(SwingConstants.TOP);
		onglets.setPreferredSize(new Dimension(1000,1000));

		//ONGLET 1
		JPanel onglet1 = new JPanel();
		JLabel label;
		label = new JLabel();
		label.setText("<html><body><p style='text-align:center'>Ouverture du fichier"+csvPath+"/"+csvFileName+"<br>Bienvenue sur l'application RespireStat<br /><br />Cliquez sur un des onglets pour accÃ©der aux statistiques</p></body></html>");
		panel.add(label);
		onglet1.add(label);


		onglets.addTab("Accueil", onglet1);


		//ONGLET 2
		JPanel onglet2 = new JPanel();
		JLabel labelTableau=new JLabel();
		Etablissement etabNO2,etabPM10,etabPM25;
		JTable table;
		JScrollPane spane;
		TableauStat1 tab1;

		onglet2.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		for(int annee=2012; annee<=2017; annee++) {

			labelTableau.setText(" "+annee);

			etabNO2 = StatEtab.getPlusPolluantNO2(ConvertCSV.listeEtab, annee);
			etabPM10 = StatEtab.getPlusPolluantPM10(ConvertCSV.listeEtab, annee);			
			etabPM25 = StatEtab.getPlusPolluantPM25(ConvertCSV.listeEtab, annee);
			
			if(etabNO2!=null && etabPM10!=null && etabPM25!=null) {
				Etablissement[] etabs = new Etablissement[3];
				etabs[0] = etabNO2;
				etabs[1] = etabPM10;
				etabs[2] = etabPM25;tab1 = new TableauStat1(etabs, annee);

				table = new JTable(tab1);
				spane =new JScrollPane(table);

				table.setCellSelectionEnabled(false);

				table.setPreferredSize(new Dimension(500,95));
				table.setPreferredScrollableViewportSize(table.getPreferredSize());
				table.setFillsViewportHeight(true);

				//onglet2.add(labelTableau,c);
				onglet2.add(spane,c);
				c.gridy++;
			}
			
		}

		onglets.addTab("Les plus polluants", onglet2);

		//ONGLET 3
				JPanel onglet3 = new JPanel();

				HashMap<String, Double> test = new HashMap<String, Double>();
				HashMap<String, Double> moyenneVillesPM10 = new HashMap<String, Double>();
				HashMap<String, Double> moyenneVillesPM25 = new HashMap<String, Double>();
				for(String ville : ConvertCSV.listeVilles) {
					test.put(ville, StatEtab.getMoyennePolluantNO2Ville(ConvertCSV.listeEtab, ville, 2017));
					moyenneVillesPM10.put(ville, StatEtab.getMoyennePolluantPM10Ville(ConvertCSV.listeEtab, ville, 2017));
					moyenneVillesPM25.put(ville, StatEtab.getMoyennePolluantPM25Ville(ConvertCSV.listeEtab, ville, 2017));
				

				}

				TableauStat2 tab2 = new TableauStat2(test, moyenneVillesPM10,moyenneVillesPM25);

				table = new JTable(tab2);
				spane = new JScrollPane(table);

				onglet3.add(spane);

				onglets.addTab("Moyenne par ville 2017", onglet3);

				//ONGLET 4
				JPanel onglet4 = new JPanel();
				

				HashMap<String, Double> moyenneDepartementNO2 = new HashMap<String, Double>();
				HashMap<String, Double> moyenneDepartementPM10 = new HashMap<String, Double>();
				HashMap<String, Double> moyenneDepartementPM25 = new HashMap<String, Double>();
			
				
				for(String departement : ConvertCSV.listeDepartements) {
					moyenneDepartementNO2.put(departement, StatEtab.getMoyennePolluantNO2Dpt(ConvertCSV.listeEtab, departement, 2017));
					moyenneDepartementPM10.put(departement, StatEtab.getMoyennePolluantPM10Dpt(ConvertCSV.listeEtab, departement, 2017));
					moyenneDepartementPM25.put(departement, StatEtab.getMoyennePolluantPM25Dpt(ConvertCSV.listeEtab, departement, 2017));
					
					
				}
				
		TableauStat3 tab3 = new TableauStat3( moyenneDepartementNO2,  moyenneDepartementPM10, moyenneDepartementPM25);
				
				table = new JTable(tab3);
				spane = new JScrollPane(table);

				onglet4.add(spane);

				
				onglets.addTab("Moyenne par département 2017", onglet4);
				
				//ONGLET 5
				JPanel onglet5 = new JPanel();
				
				HashMap<String, Double> moyenneDepartementNO22012 = new HashMap<String, Double>();
				HashMap<String, Double> moyenneDepartementPM102012 = new HashMap<String, Double>();
				HashMap<String, Double> moyenneDepartementPM252012 = new HashMap<String, Double>();

				HashMap<String, Double> moyenneDepartementNO22013 = new HashMap<String, Double>();
				HashMap<String, Double> moyenneDepartementPM102013 = new HashMap<String, Double>();
				HashMap<String, Double> moyenneDepartementPM252013 = new HashMap<String, Double>();

				HashMap<String, Double> moyenneDepartementNO22014 = new HashMap<String, Double>();
				HashMap<String, Double> moyenneDepartementPM102014 = new HashMap<String, Double>();
				HashMap<String, Double> moyenneDepartementPM252014 = new HashMap<String, Double>();

				HashMap<String, Double> moyenneDepartementNO22015 = new HashMap<String, Double>();
				HashMap<String, Double> moyenneDepartementPM102015 = new HashMap<String, Double>();
				HashMap<String, Double> moyenneDepartementPM252015 = new HashMap<String, Double>();

				HashMap<String, Double> moyenneDepartementNO22016 = new HashMap<String, Double>();
				HashMap<String, Double> moyenneDepartementPM102016 = new HashMap<String, Double>();
				HashMap<String, Double> moyenneDepartementPM252016 = new HashMap<String, Double>();

				HashMap<String, Double> moyenneDepartementNO22017 = new HashMap<String, Double>();
				HashMap<String, Double> moyenneDepartementPM102017 = new HashMap<String, Double>();
				HashMap<String, Double> moyenneDepartementPM252017 = new HashMap<String, Double>();
			
					for(int annee=2012; annee<=2017; annee++) 
						{
			for(String departement : ConvertCSV.listeDepartements) 
				
						{
				if (annee == 2012) {
				moyenneDepartementNO22012.put(departement, StatEtab.getMoyennePolluantNO2Dpt(ConvertCSV.listeEtab, departement, annee));
				moyenneDepartementPM102012.put(departement, StatEtab.getMoyennePolluantPM10Dpt(ConvertCSV.listeEtab, departement, annee));
				moyenneDepartementPM252012.put(departement, StatEtab.getMoyennePolluantPM25Dpt(ConvertCSV.listeEtab, departement, annee));
				}
				if (annee == 2013) {
					moyenneDepartementNO22013.put(departement, StatEtab.getMoyennePolluantNO2Dpt(ConvertCSV.listeEtab, departement, annee));
					moyenneDepartementPM102013.put(departement, StatEtab.getMoyennePolluantPM10Dpt(ConvertCSV.listeEtab, departement, annee));
					moyenneDepartementPM252013.put(departement, StatEtab.getMoyennePolluantPM25Dpt(ConvertCSV.listeEtab, departement, annee));
					}
				if (annee == 2014) {
					moyenneDepartementNO22014.put(departement, StatEtab.getMoyennePolluantNO2Dpt(ConvertCSV.listeEtab, departement, annee));
					moyenneDepartementPM102014.put(departement, StatEtab.getMoyennePolluantPM10Dpt(ConvertCSV.listeEtab, departement, annee));
					moyenneDepartementPM252014.put(departement, StatEtab.getMoyennePolluantPM25Dpt(ConvertCSV.listeEtab, departement, annee));
					}
				if (annee == 2015) {
					moyenneDepartementNO22015.put(departement, StatEtab.getMoyennePolluantNO2Dpt(ConvertCSV.listeEtab, departement, annee));
					moyenneDepartementPM102015.put(departement, StatEtab.getMoyennePolluantPM10Dpt(ConvertCSV.listeEtab, departement, annee));
					moyenneDepartementPM252015.put(departement, StatEtab.getMoyennePolluantPM25Dpt(ConvertCSV.listeEtab, departement, annee));
					}
				if (annee == 2016) {
					moyenneDepartementNO22016.put(departement, StatEtab.getMoyennePolluantNO2Dpt(ConvertCSV.listeEtab, departement, annee));
					moyenneDepartementPM102016.put(departement, StatEtab.getMoyennePolluantPM10Dpt(ConvertCSV.listeEtab, departement, annee));
					moyenneDepartementPM252016.put(departement, StatEtab.getMoyennePolluantPM25Dpt(ConvertCSV.listeEtab, departement, annee));
					}
				if (annee == 2017) {
					moyenneDepartementNO22017.put(departement, StatEtab.getMoyennePolluantNO2Dpt(ConvertCSV.listeEtab, departement, annee));
					moyenneDepartementPM102017.put(departement, StatEtab.getMoyennePolluantPM10Dpt(ConvertCSV.listeEtab, departement, annee));
					moyenneDepartementPM252017.put(departement, StatEtab.getMoyennePolluantPM25Dpt(ConvertCSV.listeEtab, departement, annee));
					}
						}	
			if (annee == 2012) {
			TableauStat4 tab2012 = new TableauStat4( moyenneDepartementNO22012,  moyenneDepartementPM102012, moyenneDepartementPM252012,annee);
			table = new JTable(tab2012);
			spane = new JScrollPane(table);

			table.setCellSelectionEnabled(false);

			table.setPreferredSize(new Dimension(500,80));
			table.setPreferredScrollableViewportSize(table.getPreferredSize());
			table.setFillsViewportHeight(true);

				onglet5.add(spane);
				
			}
			if (annee == 2013) {
				TableauStat4 tab2013 = new TableauStat4( moyenneDepartementNO22013,  moyenneDepartementPM102013, moyenneDepartementPM252013,annee);
				table = new JTable(tab2013);
				spane = new JScrollPane(table);
				table.setCellSelectionEnabled(false);

				table.setPreferredSize(new Dimension(500,80));
				table.setPreferredScrollableViewportSize(table.getPreferredSize());
				table.setFillsViewportHeight(true);
				
					onglet5.add(spane);
					
				}
			if (annee == 2014) {
				TableauStat4 tab2014 = new TableauStat4( moyenneDepartementNO22014,  moyenneDepartementPM102014, moyenneDepartementPM252014,annee);
				table = new JTable(tab2014);
				spane = new JScrollPane(table);

				table.setCellSelectionEnabled(false);

				table.setPreferredSize(new Dimension(500,80));
				table.setPreferredScrollableViewportSize(table.getPreferredSize());
				table.setFillsViewportHeight(true);

					onglet5.add(spane);
					
				}
			if (annee == 2015) {
				TableauStat4 tab2015 = new TableauStat4( moyenneDepartementNO22015,  moyenneDepartementPM102015, moyenneDepartementPM252015,annee);
				table = new JTable(tab2015);
				spane = new JScrollPane(table);

				table.setCellSelectionEnabled(false);

				table.setPreferredSize(new Dimension(500,80));
				table.setPreferredScrollableViewportSize(table.getPreferredSize());
				table.setFillsViewportHeight(true);

					onglet5.add(spane);
					
				}
			if (annee == 2016) {
				TableauStat4 tab2016 = new TableauStat4( moyenneDepartementNO22016,  moyenneDepartementPM102016, moyenneDepartementPM252016,annee);
				table = new JTable(tab2016);
				spane = new JScrollPane(table);

				table.setCellSelectionEnabled(false);

				table.setPreferredSize(new Dimension(500,80));
				table.setPreferredScrollableViewportSize(table.getPreferredSize());
				table.setFillsViewportHeight(true);

					onglet5.add(spane);
					
				}
			if (annee == 2017) {
					TableauStat4 tab2017 = new TableauStat4( moyenneDepartementNO22017,  moyenneDepartementPM102017, moyenneDepartementPM252017,annee);
					table = new JTable(tab2017);
					spane = new JScrollPane(table);

					table.setCellSelectionEnabled(false);

					table.setPreferredSize(new Dimension(500,80));
					table.setPreferredScrollableViewportSize(table.getPreferredSize());
					table.setFillsViewportHeight(true);

						onglet5.add(spane);
						
					}
			
								onglets.addTab("Moyenne par département par annee", onglet5);
						}
				panel.add(onglets);

			



				return panel;
		}

		}