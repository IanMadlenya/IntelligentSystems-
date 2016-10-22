package agents;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;

import jade.core.*;
import jade.core.Runtime;
import jade.gui.*;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import ontologies.*;
import utility.Utility;

public class ExchangeAgentGui extends JFrame implements ActionListener, SupplierVocabulary {
	private Random rnd = Utility.newRandom(hashCode());
	
	// Save radio button settings
	private String[] aRetailerNames = new String[3];
	private int[] aRetailerGenRates = new int[3];
	private int[] aRetailerPrices = new int[3];
	private int[] aRetailerSupplies = new int[3];
	
	// Components
	// Retailers
	private JRadioButton rb1, rb2, rb3;
	private int selectedRetailer = 0;
	private JTextField fRetailerName;
	
	private JSlider sRetailerGenRate;
	private JSlider sRetailerPrice;
	private JSlider sRetailerSupply;
	
	private JLabel lRetailerGenRateValue;
	private JLabel lRetailerPriceValue;
	private JLabel lRetailerSupplyValue; 
	
	private JCheckBox cbRandRetailerGenRate;
	private JCheckBox cbRandRetailerPrice;
	private JCheckBox cbRandRetailerSupply;
	
	private SliderListener slRetailerGenRate;
	private SliderListener slRetailerPrice;
	private SliderListener slRetailerSupply;
	
	private CheckBoxListener cblRetailerGenRate;
	private CheckBoxListener cblRetailerPrice;
	private CheckBoxListener cblRetailerSupply;
	
	// Home
	private JTextField fHomeName;
	private JSlider sHomeGenRate;
	private JSlider sHomeUseRate;
	private JSlider sHomeIncome;
	private JSlider sHomeSupply;

	public ExchangeAgentGui() {
		JPanel pHeader = new JPanel();
		getContentPane().add(pHeader, BorderLayout.NORTH);
		pHeader.setLayout(new BoxLayout(pHeader, BoxLayout.Y_AXIS));
		
		JLabel lTitle = new JLabel("Home Energy Trading System");
		lTitle.setFont(new Font("Calibri", Font.BOLD, 32));
		lTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		pHeader.add(lTitle);
		
		JLabel lNames = new JLabel("Designed by James Martin, Nathan Harris & Jacyln Seychell");
		lNames.setAlignmentX(Component.CENTER_ALIGNMENT);
		lNames.setFont(new Font("Calibri", Font.PLAIN, 24));
		pHeader.add(lNames);
		
		JSeparator separator = new JSeparator();
		pHeader.add(separator);
		
		JPanel pBody = new JPanel();
		getContentPane().add(pBody, BorderLayout.CENTER);
		pBody.setLayout(new GridLayout(1, 3, 0, 0));
		
		JPanel pHome = new JPanel();
		pBody.add(pHome);
		GridBagLayout gbl_pHome = new GridBagLayout();
		gbl_pHome.columnWidths = new int[]{0, 0};
		gbl_pHome.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_pHome.columnWeights = new double[]{1.0, 1.0};
		gbl_pHome.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		pHome.setLayout(gbl_pHome);
		
		JLabel lHomeTitle = new JLabel("Home Agent Parameters");
		lHomeTitle.setFont(new Font("Calibri", Font.PLAIN, 24));
		GridBagConstraints gbc_lHomeTitle = new GridBagConstraints();
		gbc_lHomeTitle.gridwidth = 4;
		gbc_lHomeTitle.insets = new Insets(0, 0, 5, 0);
		gbc_lHomeTitle.gridx = 0;
		gbc_lHomeTitle.gridy = 0;
		pHome.add(lHomeTitle, gbc_lHomeTitle);
		
		JLabel lHomeName = new JLabel("Name:");
		GridBagConstraints gbc_lHomeName = new GridBagConstraints();
		gbc_lHomeName.insets = new Insets(0, 0, 5, 5);
		gbc_lHomeName.anchor = GridBagConstraints.EAST;
		gbc_lHomeName.gridx = 0;
		gbc_lHomeName.gridy = 2;
		pHome.add(lHomeName, gbc_lHomeName);
		
		fHomeName = new JTextField();
		fHomeName.setText("HomeAgent");
		GridBagConstraints gbc_fHomeName = new GridBagConstraints();
		gbc_fHomeName.insets = new Insets(0, 0, 5, 5);
		gbc_fHomeName.fill = GridBagConstraints.HORIZONTAL;
		gbc_fHomeName.gridx = 1;
		gbc_fHomeName.gridy = 2;
		pHome.add(fHomeName, gbc_fHomeName);
		fHomeName.setColumns(10);
		
		JLabel lHomeGenRate = new JLabel("Generation Rate:");
		GridBagConstraints gbc_lHomeGenRate = new GridBagConstraints();
		gbc_lHomeGenRate.anchor = GridBagConstraints.EAST;
		gbc_lHomeGenRate.insets = new Insets(0, 0, 5, 5);
		gbc_lHomeGenRate.gridx = 0;
		gbc_lHomeGenRate.gridy = 3;
		pHome.add(lHomeGenRate, gbc_lHomeGenRate);
		
		sHomeGenRate = new JSlider();
		sHomeGenRate.setMajorTickSpacing(10);
		sHomeGenRate.setPaintTicks(true);
		GridBagConstraints gbc_sHomeGenRate = new GridBagConstraints();
		gbc_sHomeGenRate.fill = GridBagConstraints.HORIZONTAL;
		gbc_sHomeGenRate.insets = new Insets(0, 0, 5, 5);
		gbc_sHomeGenRate.gridx = 1;
		gbc_sHomeGenRate.gridy = 3;
		pHome.add(sHomeGenRate, gbc_sHomeGenRate);
		
		JLabel lHomeGenValue = new JLabel("genValue");
		lHomeGenValue.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lHomeGenValue = new GridBagConstraints();
		gbc_lHomeGenValue.insets = new Insets(0, 0, 5, 5);
		gbc_lHomeGenValue.gridx = 2;
		gbc_lHomeGenValue.gridy = 3;
		
		pHome.add(lHomeGenValue, gbc_lHomeGenValue);
		lHomeGenValue.setText(Integer.toString(sHomeGenRate.getValue()));
		
		JCheckBox cbRandHomeGenRate = new JCheckBox("Randomize");
		GridBagConstraints gbc_cbRandHomeGenRate = new GridBagConstraints();
		gbc_cbRandHomeGenRate.insets = new Insets(0, 0, 5, 0);
		gbc_cbRandHomeGenRate.gridx = 3;
		gbc_cbRandHomeGenRate.gridy = 3;
		pHome.add(cbRandHomeGenRate, gbc_cbRandHomeGenRate);
		
		JLabel lHomeUseRate = new JLabel("Usage Rate:");
		GridBagConstraints gbc_lHomeUseRate = new GridBagConstraints();
		gbc_lHomeUseRate.anchor = GridBagConstraints.EAST;
		gbc_lHomeUseRate.insets = new Insets(0, 0, 5, 5);
		gbc_lHomeUseRate.gridx = 0;
		gbc_lHomeUseRate.gridy = 4;
		pHome.add(lHomeUseRate, gbc_lHomeUseRate);
		
		sHomeUseRate = new JSlider();
		sHomeUseRate.setMajorTickSpacing(10);
		sHomeUseRate.setValue(55);
		sHomeUseRate.setMinimum(10);
		sHomeUseRate.setPaintTicks(true);
		GridBagConstraints gbc_sHomeUseRate = new GridBagConstraints();
		gbc_sHomeUseRate.fill = GridBagConstraints.HORIZONTAL;
		gbc_sHomeUseRate.insets = new Insets(0, 0, 5, 5);
		gbc_sHomeUseRate.gridx = 1;
		gbc_sHomeUseRate.gridy = 4;
		pHome.add(sHomeUseRate, gbc_sHomeUseRate);
		
		JLabel lHomeUseValue = new JLabel();
		lHomeUseValue.setText(Integer.toString(sHomeUseRate.getValue()));
		GridBagConstraints gbc_lHomeUseValue = new GridBagConstraints();
		gbc_lHomeUseValue.insets = new Insets(0, 0, 5, 5);
		gbc_lHomeUseValue.gridx = 2;
		gbc_lHomeUseValue.gridy = 4;
		pHome.add(lHomeUseValue, gbc_lHomeUseValue);
		
		JCheckBox cbRandHomeUseRate = new JCheckBox("Randomize");
		GridBagConstraints gbc_cbRandHomeUseRate = new GridBagConstraints();
		gbc_cbRandHomeUseRate.insets = new Insets(0, 0, 5, 0);
		gbc_cbRandHomeUseRate.gridx = 3;
		gbc_cbRandHomeUseRate.gridy = 4;
		pHome.add(cbRandHomeUseRate, gbc_cbRandHomeUseRate);
		
		JLabel lHomeSupply = new JLabel("Supply:");
		GridBagConstraints gbc_lHomeSupply = new GridBagConstraints();
		gbc_lHomeSupply.anchor = GridBagConstraints.EAST;
		gbc_lHomeSupply.insets = new Insets(0, 0, 5, 5);
		gbc_lHomeSupply.gridx = 0;
		gbc_lHomeSupply.gridy = 5;
		pHome.add(lHomeSupply, gbc_lHomeSupply);
		
		sHomeSupply = new JSlider();
		sHomeSupply.setMaximum(2000);
		sHomeSupply.setPaintTicks(true);
		sHomeSupply.setValue(1000);
		sHomeSupply.setMajorTickSpacing(100);
		GridBagConstraints gbc_sHomeSupply = new GridBagConstraints();
		gbc_sHomeSupply.fill = GridBagConstraints.HORIZONTAL;
		gbc_sHomeSupply.insets = new Insets(0, 0, 5, 5);
		gbc_sHomeSupply.gridx = 1;
		gbc_sHomeSupply.gridy = 5;
		pHome.add(sHomeSupply, gbc_sHomeSupply);
		
		JLabel lHomeSupplyValue = new JLabel();
		lHomeSupplyValue.setText(Integer.toString(sHomeSupply.getValue()));
		GridBagConstraints gbc_lHomeSupplyValue = new GridBagConstraints();
		gbc_lHomeSupplyValue.insets = new Insets(0, 0, 5, 5);
		gbc_lHomeSupplyValue.gridx = 2;
		gbc_lHomeSupplyValue.gridy = 5;
		pHome.add(lHomeSupplyValue, gbc_lHomeSupplyValue);
		
		
		JCheckBox cbRandHomeSupply = new JCheckBox("Randomize");
		GridBagConstraints gbc_cbRandHomeSupply = new GridBagConstraints();
		gbc_cbRandHomeSupply.insets = new Insets(0, 0, 5, 0);
		gbc_cbRandHomeSupply.gridx = 3;
		gbc_cbRandHomeSupply.gridy = 5;
		pHome.add(cbRandHomeSupply, gbc_cbRandHomeSupply);
		
		JLabel lHomeIncome = new JLabel("Income:");
		GridBagConstraints gbc_lHomeIncome = new GridBagConstraints();
		gbc_lHomeIncome.anchor = GridBagConstraints.EAST;
		gbc_lHomeIncome.insets = new Insets(0, 0, 5, 5);
		gbc_lHomeIncome.gridx = 0;
		gbc_lHomeIncome.gridy = 6;
		pHome.add(lHomeIncome, gbc_lHomeIncome);
		
		sHomeIncome = new JSlider();
		sHomeIncome.setMinimum(10);
		sHomeIncome.setValue(55);
		sHomeIncome.setMajorTickSpacing(10);
		sHomeIncome.setPaintTicks(true);
		GridBagConstraints gbc_sHomeIncome = new GridBagConstraints();
		gbc_sHomeIncome.fill = GridBagConstraints.HORIZONTAL;
		gbc_sHomeIncome.insets = new Insets(0, 0, 5, 5);
		gbc_sHomeIncome.gridx = 1;
		gbc_sHomeIncome.gridy = 6;
		pHome.add(sHomeIncome, gbc_sHomeIncome);
		
		JLabel lHomeIncomeValue = new JLabel("");
		lHomeIncomeValue.setText(Integer.toString(sHomeIncome.getValue()));
		GridBagConstraints gbc_lHomeIncomeValue = new GridBagConstraints();
		gbc_lHomeIncomeValue.insets = new Insets(0, 0, 5, 5);
		gbc_lHomeIncomeValue.gridx = 2;
		gbc_lHomeIncomeValue.gridy = 6;
		pHome.add(lHomeIncomeValue, gbc_lHomeIncomeValue);
				
		JCheckBox cbRandHomeIncome = new JCheckBox("Randomize");
		GridBagConstraints gbc_cbRandHomeIncome = new GridBagConstraints();
		gbc_cbRandHomeIncome.insets = new Insets(0, 0, 5, 0);
		gbc_cbRandHomeIncome.gridx = 3;
		gbc_cbRandHomeIncome.gridy = 6;
		pHome.add(cbRandHomeIncome, gbc_cbRandHomeIncome);
		
		JPanel pRetailers = new JPanel();
		pBody.add(pRetailers);
		GridBagLayout gbl_pRetailers = new GridBagLayout();
		gbl_pRetailers.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_pRetailers.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_pRetailers.columnWeights = new double[]{1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_pRetailers.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		pRetailers.setLayout(gbl_pRetailers);
		
		JLabel lRetailerTitle = new JLabel("Retailer Agents Parameters");
		lRetailerTitle.setFont(new Font("Calibri", Font.PLAIN, 24));
		GridBagConstraints gbc_lRetailerTitle = new GridBagConstraints();
		gbc_lRetailerTitle.gridheight = 2;
		gbc_lRetailerTitle.gridwidth = 4;
		gbc_lRetailerTitle.insets = new Insets(0, 0, 5, 0);
		gbc_lRetailerTitle.gridx = 0;
		gbc_lRetailerTitle.gridy = 0;
		pRetailers.add(lRetailerTitle, gbc_lRetailerTitle);
		
		JLabel lRetailerSelect = new JLabel("Retailer:");
		GridBagConstraints gbc_lRetailerSelect = new GridBagConstraints();
		gbc_lRetailerSelect.anchor = GridBagConstraints.EAST;
		gbc_lRetailerSelect.insets = new Insets(0, 0, 5, 5);
		gbc_lRetailerSelect.gridx = 0;
		gbc_lRetailerSelect.gridy = 2;
		pRetailers.add(lRetailerSelect, gbc_lRetailerSelect);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 2;
		pRetailers.add(panel, gbc_panel);
		
		rb1 = new JRadioButton("1");
		rb1.setSelected(true);
		panel.add(rb1);
		
		rb2 = new JRadioButton("2");
		panel.add(rb2);
		
		rb3 = new JRadioButton("3");
		panel.add(rb3);
		
		ButtonGroup rbg = new ButtonGroup();
		rbg.add(rb1);
		rbg.add(rb2);
		rbg.add(rb3);
		
		JLabel lRetailerName = new JLabel("Name:");
		GridBagConstraints gbc_lRetailerName = new GridBagConstraints();
		gbc_lRetailerName.anchor = GridBagConstraints.EAST;
		gbc_lRetailerName.insets = new Insets(0, 0, 5, 5);
		gbc_lRetailerName.gridx = 0;
		gbc_lRetailerName.gridy = 3;
		pRetailers.add(lRetailerName, gbc_lRetailerName);
		
		fRetailerName = new JTextField();
		fRetailerName.setText("R1");
		fRetailerName.setColumns(10);
		GridBagConstraints gbc_fRetailerName = new GridBagConstraints();
		gbc_fRetailerName.fill = GridBagConstraints.HORIZONTAL;
		gbc_fRetailerName.insets = new Insets(0, 0, 5, 5);
		gbc_fRetailerName.gridx = 1;
		gbc_fRetailerName.gridy = 3;
		pRetailers.add(fRetailerName, gbc_fRetailerName);
		
		JLabel lRetailerGenRate = new JLabel("Generation Rate:");
		GridBagConstraints gbc_lRetailerGenRate = new GridBagConstraints();
		gbc_lRetailerGenRate.anchor = GridBagConstraints.EAST;
		gbc_lRetailerGenRate.insets = new Insets(0, 0, 5, 5);
		gbc_lRetailerGenRate.gridx = 0;
		gbc_lRetailerGenRate.gridy = 4;
		pRetailers.add(lRetailerGenRate, gbc_lRetailerGenRate);
		
		sRetailerGenRate = new JSlider();
		sRetailerGenRate.setPaintTicks(true);
		sRetailerGenRate.setMajorTickSpacing(10);
		GridBagConstraints gbc_sRetailerGenRate = new GridBagConstraints();
		gbc_sRetailerGenRate.fill = GridBagConstraints.HORIZONTAL;
		gbc_sRetailerGenRate.insets = new Insets(0, 0, 5, 5);
		gbc_sRetailerGenRate.gridx = 1;
		gbc_sRetailerGenRate.gridy = 4;
		pRetailers.add(sRetailerGenRate, gbc_sRetailerGenRate);
		
		lRetailerGenRateValue = new JLabel("50");
		lRetailerGenRateValue.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lRetailerGenRateValue = new GridBagConstraints();
		gbc_lRetailerGenRateValue.insets = new Insets(0, 0, 5, 5);
		gbc_lRetailerGenRateValue.gridx = 2;
		gbc_lRetailerGenRateValue.gridy = 4;
		pRetailers.add(lRetailerGenRateValue, gbc_lRetailerGenRateValue);
		
		cbRandRetailerGenRate = new JCheckBox("Randomize");
		GridBagConstraints gbc_cbRandRetailerGenRate = new GridBagConstraints();
		gbc_cbRandRetailerGenRate.insets = new Insets(0, 0, 5, 0);
		gbc_cbRandRetailerGenRate.gridx = 3;
		gbc_cbRandRetailerGenRate.gridy = 4;
		pRetailers.add(cbRandRetailerGenRate, gbc_cbRandRetailerGenRate);
		
		JLabel lRetailerPrice = new JLabel("Price:");
		GridBagConstraints gbc_lRetailerPrice = new GridBagConstraints();
		gbc_lRetailerPrice.anchor = GridBagConstraints.EAST;
		gbc_lRetailerPrice.insets = new Insets(0, 0, 5, 5);
		gbc_lRetailerPrice.gridx = 0;
		gbc_lRetailerPrice.gridy = 5;
		pRetailers.add(lRetailerPrice, gbc_lRetailerPrice);
		
		sRetailerPrice = new JSlider();
		sRetailerPrice.setValue(55);
		sRetailerPrice.setPaintTicks(true);
		sRetailerPrice.setMinimum(10);
		sRetailerPrice.setMajorTickSpacing(10);
		GridBagConstraints gbc_sRetailerPrice = new GridBagConstraints();
		gbc_sRetailerPrice.fill = GridBagConstraints.HORIZONTAL;
		gbc_sRetailerPrice.insets = new Insets(0, 0, 5, 5);
		gbc_sRetailerPrice.gridx = 1;
		gbc_sRetailerPrice.gridy = 5;
		pRetailers.add(sRetailerPrice, gbc_sRetailerPrice);
		
		lRetailerPriceValue = new JLabel("55");
		GridBagConstraints gbc_lRetailerPriceValue = new GridBagConstraints();
		gbc_lRetailerPriceValue.insets = new Insets(0, 0, 5, 5);
		gbc_lRetailerPriceValue.gridx = 2;
		gbc_lRetailerPriceValue.gridy = 5;
		pRetailers.add(lRetailerPriceValue, gbc_lRetailerPriceValue);
		
		cbRandRetailerPrice = new JCheckBox("Randomize");
		GridBagConstraints gbc_cbRandRetailerPrice = new GridBagConstraints();
		gbc_cbRandRetailerPrice.insets = new Insets(0, 0, 5, 0);
		gbc_cbRandRetailerPrice.gridx = 3;
		gbc_cbRandRetailerPrice.gridy = 5;
		pRetailers.add(cbRandRetailerPrice, gbc_cbRandRetailerPrice);
		
		JLabel lRetailerSupply = new JLabel("Supply:");
		GridBagConstraints gbc_lRetailerSupply = new GridBagConstraints();
		gbc_lRetailerSupply.anchor = GridBagConstraints.EAST;
		gbc_lRetailerSupply.insets = new Insets(0, 0, 5, 5);
		gbc_lRetailerSupply.gridx = 0;
		gbc_lRetailerSupply.gridy = 6;
		pRetailers.add(lRetailerSupply, gbc_lRetailerSupply);
		
		sRetailerSupply = new JSlider();
		sRetailerSupply.setMaximum(5000);
		sRetailerSupply.setValue(2500);
		sRetailerSupply.setPaintTicks(true);
		sRetailerSupply.setMajorTickSpacing(500);
		GridBagConstraints gbc_sRetailerSupply = new GridBagConstraints();
		gbc_sRetailerSupply.fill = GridBagConstraints.HORIZONTAL;
		gbc_sRetailerSupply.insets = new Insets(0, 0, 5, 5);
		gbc_sRetailerSupply.gridx = 1;
		gbc_sRetailerSupply.gridy = 6;
		pRetailers.add(sRetailerSupply, gbc_sRetailerSupply);
		
		lRetailerSupplyValue = new JLabel("1000");
		GridBagConstraints gbc_lRetailerSupplyValue = new GridBagConstraints();
		gbc_lRetailerSupplyValue.insets = new Insets(0, 0, 5, 5);
		gbc_lRetailerSupplyValue.gridx = 2;
		gbc_lRetailerSupplyValue.gridy = 6;
		pRetailers.add(lRetailerSupplyValue, gbc_lRetailerSupplyValue);
		
		cbRandRetailerSupply = new JCheckBox("Randomize");
		GridBagConstraints gbc_cbRandRetailerSupply = new GridBagConstraints();
		gbc_cbRandRetailerSupply.insets = new Insets(0, 0, 5, 0);
		gbc_cbRandRetailerSupply.gridx = 3;
		gbc_cbRandRetailerSupply.gridy = 6;
		pRetailers.add(cbRandRetailerSupply, gbc_cbRandRetailerSupply);
		
		// Setup default retailer settings
		for(int i = 0; i < 3; i++) {
			aRetailerNames[i] = "R"+(i+1);
			aRetailerGenRates[i] = sRetailerGenRate.getValue();
			aRetailerPrices[i] = sRetailerPrice.getValue();
			aRetailerSupplies[i] = sRetailerSupply.getValue();
		}
		
		// Handle Slider events
		sHomeIncome.addChangeListener(new SliderListener(lHomeIncomeValue));
		sHomeSupply.addChangeListener(new SliderListener(lHomeSupplyValue));
		sHomeGenRate.addChangeListener(new SliderListener(lHomeGenValue));
		sHomeUseRate.addChangeListener(new SliderListener(lHomeUseValue));
		
		slRetailerGenRate = new SliderListener(lRetailerGenRateValue);
		slRetailerPrice = new SliderListener(lRetailerPriceValue);
		slRetailerSupply = new SliderListener(lRetailerSupplyValue);
		
		sRetailerGenRate.addChangeListener(slRetailerGenRate);
		sRetailerPrice.addChangeListener(slRetailerPrice);
		sRetailerSupply.addChangeListener(slRetailerSupply);
		
		// Handle CheckBox events
		cbRandHomeUseRate.addChangeListener(new CheckBoxListener(lHomeUseValue, sHomeUseRate));
		cbRandHomeGenRate.addChangeListener(new CheckBoxListener(lHomeGenValue, sHomeGenRate));
		cbRandHomeIncome.addChangeListener(new CheckBoxListener(lHomeIncomeValue, sHomeIncome));
		cbRandHomeSupply.addChangeListener(new CheckBoxListener(lHomeSupplyValue, sHomeSupply));

		cblRetailerGenRate = new CheckBoxListener(lRetailerGenRateValue, sRetailerGenRate);
		cblRetailerPrice = new CheckBoxListener(lRetailerPriceValue, sRetailerPrice);
		cblRetailerSupply = new CheckBoxListener(lRetailerSupplyValue, sRetailerSupply);
		
		JPanel pSetup = new JPanel();
		getContentPane().add(pSetup, BorderLayout.SOUTH);
		
		JButton bQuickStart = new JButton("Quickstart!");
		pSetup.add(bQuickStart);
		
		JButton bStart = new JButton("Start");
		pSetup.add(bStart);
		
		bQuickStart.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				initalizeJade(true);
				bStart.setEnabled(false);
				bQuickStart.setEnabled(false);
			}
		});
		
		bStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				initalizeJade(false);
				bStart.setEnabled(false);
				bQuickStart.setEnabled(false);
			}
		});
		
		cbRandRetailerGenRate.addChangeListener(cblRetailerGenRate);
		cbRandRetailerPrice.addChangeListener(cblRetailerPrice);
		cbRandRetailerSupply.addChangeListener(cblRetailerSupply);
		
		// Handle RadioButton events
		rb1.addActionListener(new RadioButtonListener(0));
		rb2.addActionListener(new RadioButtonListener(1));
		rb3.addActionListener(new RadioButtonListener(2));
		
		setSize(1280, 720);
	} 
	
	// Random slider value
	int randomSliderValue(JSlider slider) {
		return (rnd.nextInt(slider.getMaximum()) + slider.getMinimum()) % slider.getMaximum(); 
	}
	
	// Custom JSlider listener
	public class SliderListener implements ChangeListener {
		private JLabel label;
		private int value;
		
		SliderListener(JLabel label) {
			super();
			this.label = label;
		}
		
		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider)e.getSource();
			label.setText(Integer.toString(source.getValue()));
		}
	}

	// Custom JCheckBox listener
	public class CheckBoxListener implements ChangeListener {
		private JSlider slider;
		private JLabel label;
		
		CheckBoxListener(JLabel label, JSlider slider) {
			super();
			this.label = label;
			this.slider = slider;
		}
		
		@Override
		public void stateChanged(ChangeEvent e) {
			JCheckBox source = (JCheckBox)e.getSource();
			if (source.isSelected()) {
				slider.setEnabled(false);
				label.setText("??");
			} else {
				slider.setEnabled(true);
				label.setText(Integer.toString(slider.getValue()));
			}
		}
	}
	
	// Custom JRadioButton listener
	public class RadioButtonListener implements ActionListener {
		private int retailer;
		
		RadioButtonListener(int retailer) {
			super();
			this.retailer = retailer;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JRadioButton source = (JRadioButton)e.getSource();
			if (source.isSelected() && retailer != selectedRetailer) {
				try {
					// Save last settings
					aRetailerNames[selectedRetailer] = fRetailerName.getText();
					
					if(cbRandRetailerGenRate.isSelected()) {
						cbRandRetailerGenRate.setSelected(false);
						aRetailerGenRates[selectedRetailer] = randomSliderValue(sRetailerGenRate);
					} else {
						aRetailerGenRates[selectedRetailer] = sRetailerGenRate.getValue();
					}
					
					if(cbRandRetailerPrice.isSelected()) {
						cbRandRetailerPrice.setSelected(false);
						aRetailerPrices[selectedRetailer] = randomSliderValue(sRetailerPrice);
					} else {
						
						aRetailerPrices[selectedRetailer] = sRetailerPrice.getValue();
					}
					
					if(cbRandRetailerSupply.isSelected()) {
						cbRandRetailerSupply.setSelected(false);
						aRetailerSupplies[selectedRetailer] = randomSliderValue(sRetailerSupply);
					} else {
						aRetailerSupplies[selectedRetailer] = sRetailerSupply.getValue();
					}
					
					selectedRetailer = retailer;
					
					// Update to new retailer values
					fRetailerName.setText(aRetailerNames[retailer]);
					sRetailerGenRate.setValue(aRetailerGenRates[retailer]);
					lRetailerGenRateValue.setText(Integer.toString(aRetailerGenRates[retailer]));
					sRetailerPrice.setValue(aRetailerPrices[retailer]);
					lRetailerPriceValue.setText(Integer.toString(aRetailerPrices[retailer]));
					sRetailerSupply.setValue(aRetailerSupplies[retailer]);
					lRetailerSupplyValue.setText(Integer.toString(aRetailerSupplies[retailer]));
					
					// Update listeners
					slRetailerGenRate = new SliderListener(lRetailerGenRateValue);
					slRetailerPrice = new SliderListener(lRetailerPriceValue);
					slRetailerSupply = new SliderListener(lRetailerSupplyValue);
					
					cblRetailerGenRate = new CheckBoxListener(lRetailerGenRateValue, sRetailerGenRate);
					cblRetailerPrice = new CheckBoxListener(lRetailerPriceValue, sRetailerPrice);
					cblRetailerSupply = new CheckBoxListener(lRetailerSupplyValue, sRetailerSupply);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	private void initalizeJade(boolean bQuickStart) {
		Runtime rt = Runtime.instance();
		rt.setCloseVM(true);
		
		// Create the main container 
		Profile p = new ProfileImpl(null, 1200, "main");
		jade.wrapper.AgentContainer mc = rt.createMainContainer(p);
		
		// Create container for home agents
		ProfileImpl hp = new ProfileImpl(null, 1200, "homes");
		jade.wrapper.AgentContainer hc = rt.createAgentContainer(hp);
		
		// Create container for retailer agents
		ProfileImpl rp = new ProfileImpl(null, 1200, "retailers");
		jade.wrapper.AgentContainer rc = rt.createAgentContainer(rp);
		
		String homeName = "Home";
		String[] retailerNames = new String[3];
		Object[] homeArguments;
		Object[][] retailerArguments;
		
		// Use default values or set ones
		if(!bQuickStart) {
			retailerNames = aRetailerNames;
			homeArguments = new Object[1];
			homeArguments[0] = new Home(sHomeGenRate.getValue(), sHomeUseRate.getValue(), 
					sHomeSupply.getValue(), sHomeIncome.getValue());
			
			retailerNames = aRetailerNames;
			retailerArguments = new Object[3][1];
			for(int i = 0; i < 3; i++) {
				retailerArguments[i][0] = new Retailer(aRetailerGenRates[i], aRetailerPrices[i], aRetailerSupplies[i]);
			}
		} else {
			homeArguments = new Object[0];
			retailerArguments = new Object[3][0];
			
			for(int i = 0; i < 3; i++) {
				retailerNames[i] = "Retailer " + i; 
			}
		}
		
		// Create and start agents
		try {
			AgentController rma = mc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
			rma.start();
			AgentController ba = mc.createNewAgent("Broker", "agents.BrokerAgent", new Object[0]);
			ba.start();
			
			AgentController ra;
			for(int i = 0; i < 3; i++) {
				ra = rc.createNewAgent(retailerNames[i], "agents.RetailerAgent", retailerArguments[i]);
				ra.start();
			}
			
			AgentController ha = hc.createNewAgent(homeName, "agents.HomeAgent", homeArguments);
			ha.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	
	}
	
	public static void main(String[] args) {
		ExchangeAgentGui gui = new ExchangeAgentGui();
		gui.setVisible(true);
		
		// Terminate when the main window is closed
		gui.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				System.exit(0);
		    }
		});
	}
}
