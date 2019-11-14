import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class ExamJfree extends ApplicationFrame implements ActionListener {

   private int Value, dftValue, width, height;
   JLabel label, label2, dft_la, fft_la, auco_la;
   public int fft_frq, dft_frq, aucoMax, aucoFirMax;
   public String degree = "";
   public String beef = "\u0007";
   public String dft_Chart = "dft_Chart";
   public String fft_Chart = "fft_Chart";
   public String auco_Chart = "auco_Chart";
   public ArrayList<Integer> before_dft_data = new ArrayList<>();
   public ArrayList<Double> before_fft_data = new ArrayList<>();
   public ArrayList<Integer> after_dft_data = new ArrayList<>();
   public ArrayList<Integer> after_fft_data = new ArrayList<>();
   public ArrayList<Integer> after_auco_data = new ArrayList<>();
   public int samplerate = 1024;

   public static DefaultCategoryDataset dft_dataset;
   public static DefaultCategoryDataset fft_dataset;
   public static DefaultCategoryDataset auco_dataset;
   public TimeSeriesCollection dataset;
   final TimeSeries series;
   public HashMap<String, String> map = new HashMap<>(); // key ���� ��ȣ���� �ð� ����. value ���� 1�ð� ���� ����.
   public boolean running = false;
   ByteArrayOutputStream out;
   dft dft;
   FFT fft;
  // ayTest sound;
   autocorrelation autocorrelation;
   Calculator calculator;

   JButton b1 = new JButton("capture(����)");
   JButton b2 = new JButton("stop(����)");
   // private Timer timer = new Timer(1000, this);

   public ExamJfree() {
      super("Sound Graph");

      Value = 0;
      dftValue = 0;
      width = 700;
      height = 400;
      fft_frq = 0;
      dft_frq = 0;
      aucoMax = 0;
      aucoFirMax = 0;

     // sound = new ayTest();
      dft_dataset = new DefaultCategoryDataset();
      fft_dataset = new DefaultCategoryDataset();
      auco_dataset = new DefaultCategoryDataset();
      final JPanel content = new JPanel(new BorderLayout());

      Panel btPanel = new Panel();
      Panel laPanel = new Panel();
      Panel laPanel2 = new Panel();

      Panel pan1 = new Panel();
      Panel pan2 = new Panel();
      Panel pan3 = new Panel();

      Panel sumPanel = new Panel();
      Panel btlaPanel = new Panel();
      Panel ghPanel = new Panel();
      Panel ghPanel2 = new Panel();

      JPanel dftChart = createPanel(dft_Chart);
      dftChart.setPreferredSize(new Dimension(width, height));
      JPanel fftChart = createPanel(fft_Chart);
      fftChart.setPreferredSize(new Dimension(width, height));
      JPanel aucoChart = createPanel(auco_Chart);
      aucoChart.setPreferredSize(new Dimension(width, height));

      label = new JLabel("���鹫ȣ�� Ƚ�� : 0", JLabel.CENTER);
      label2 = new JLabel("���鹫ȣ�� ���� : ����", JLabel.CENTER);
      dft = new dft();

      dft_la = new JLabel("dft ���� �ð� : 0.0��", JLabel.CENTER);
      fft_la = new JLabel("fft ���� �ð� : 0.0��", JLabel.CENTER);
      auco_la = new JLabel("autocorrelation ���� �ð� : 0.0��", JLabel.CENTER);

      laPanel2.setLayout(new GridLayout(3, 1));
      laPanel2.add(dft_la);
      laPanel2.add(fft_la);
      laPanel2.add(auco_la);

      laPanel.setLayout(new GridLayout(2, 1)); // 1�� 2��
      laPanel.add(label);
      laPanel.add(label2);

      b1.setEnabled(true);
      b2.setEnabled(false);

      btPanel.add(b1);
      btPanel.add(b2);

      ActionListener captureListener = new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            b1.setEnabled(false);
            b2.setEnabled(true);
         }
      };
      b1.addActionListener(captureListener);

      ActionListener stopListener = new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            b1.setEnabled(true);
            b2.setEnabled(false);
            running = false;
         }
      };
      b2.addActionListener(stopListener);

      series = new TimeSeries("Signal Data", Second.class);
      dataset = new TimeSeriesCollection(series);
      final JFreeChart chart = createChart(dataset);
      chart.setBackgroundPaint(Color.LIGHT_GRAY);
      final ChartPanel chartPanel = new ChartPanel(chart);

      /*
       * series2 = new TimeSeries("FFT Data", Second.class); dataset2 = new
       * TimeSeriesCollection(series2); final JFreeChart chart2 =
       * createChart(dataset2); chart2.setBackgroundPaint(Color.orange); final
       * ChartPanel chartPanel2 = new ChartPanel(chart2);
       */

      b1.addActionListener(this);
      b2.addActionListener(this);

      chartPanel.setPreferredSize(new java.awt.Dimension(width, height));
//      chartPanel2.setPreferredSize(new java.awt.Dimension(800, 500));

      ghPanel.add(chartPanel, BorderLayout.NORTH);
//      ghPanel.add(chartPanel2);

      btlaPanel.setLayout(new GridLayout(2, 1));
      btlaPanel.add(btPanel, BorderLayout.NORTH);
      btlaPanel.add(laPanel, BorderLayout.SOUTH);

      sumPanel.setLayout(new GridLayout(1, 2));
      sumPanel.add(btlaPanel, BorderLayout.WEST);
      sumPanel.add(laPanel2, BorderLayout.EAST);

      // pan1.setLayout(mgr);
      pan1.add(ghPanel);
      pan1.add(dftChart);
      pan2.add(fftChart);
      pan2.add(aucoChart);

      pan3.add(sumPanel);

      content.add(pan1, BorderLayout.NORTH);
      content.add(pan2, BorderLayout.CENTER);
      content.add(pan3, BorderLayout.SOUTH);
      setContentPane(content);

   }

   public static JPanel createPanel(String name) {
      if (name.equals("dft_Chart")) {
         JFreeChart chart = createChart(createDataset(name));
         return new ChartPanel(chart);
      } else if (name.equals("fft_Chart")) {
         JFreeChart chart = createChart2(createDataset(name));
         return new ChartPanel(chart);
      } else {
         JFreeChart chart = createChart3(createDataset(name));
         return new ChartPanel(chart);
      }
   }

   private static CategoryDataset createDataset(String name) {
      if (name.equals("dft_Chart")) {
         dft_dataset.addValue(0, "Magnitudes", "0");

         return dft_dataset;
      } else if (name.equals("fft_Chart")) {
         fft_dataset.addValue(0, "Magnitudes", "0");

         return fft_dataset;
      } else {
         auco_dataset.addValue(0, "Magnitudes", "0");

         return auco_dataset;
      }

   }

   public static JFreeChart createChart(CategoryDataset catDataset) {

      JFreeChart chart = ChartFactory.createLineChart("DFT Result", // ��Ʈ �̸�
            "DFT point", // x�� �̸�
            "Magnitude", // y�� �̸�
            catDataset, // ���� ������
            PlotOrientation.VERTICAL, // orientation
            false, // include legend
            true, // tooltips
            false // urls
      );

      chart.setBackgroundPaint(Color.white);

      CategoryPlot plot = (CategoryPlot) chart.getPlot();
      plot.setBackgroundPaint(Color.lightGray);
      plot.setRangeGridlinePaint(Color.white);

      // customise the range axis...
      NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

      // customise the renderer...
      LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
      renderer.setShapesVisible(true);
      renderer.setDrawOutlines(true);
      renderer.setUseFillPaint(true);
      renderer.setFillPaint(Color.white);

      return chart;

   }

   private static JFreeChart createChart2(CategoryDataset catDataset) {

      JFreeChart chart = ChartFactory.createLineChart("FFT Result", // ��Ʈ �̸�
            "FFT point", // x�� �̸�
            "Magnitude", // y�� �̸�
            catDataset, // ���� ������
            PlotOrientation.VERTICAL, // orientation
            false, // include legend
            true, // tooltips
            false // urls
      );

      chart.setBackgroundPaint(Color.white);

      CategoryPlot plot = (CategoryPlot) chart.getPlot();
      plot.setBackgroundPaint(Color.lightGray);
      plot.setRangeGridlinePaint(Color.white);

      // customise the range axis...
      NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

      // customise the renderer...
      LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
      renderer.setShapesVisible(true);
      renderer.setDrawOutlines(true);
      renderer.setUseFillPaint(true);
      renderer.setFillPaint(Color.white);

      return chart;

   }

   private static JFreeChart createChart3(CategoryDataset catDataset) {

      JFreeChart chart = ChartFactory.createLineChart("AUCO Result", // ��Ʈ �̸�
            "AUCO point", // x�� �̸�
            "Magnitude", // y�� �̸�
            catDataset, // ���� ������
            PlotOrientation.VERTICAL, // orientation
            false, // include legend
            true, // tooltips
            false // urls
      );

      chart.setBackgroundPaint(Color.white);

      CategoryPlot plot = (CategoryPlot) chart.getPlot();
      plot.setBackgroundPaint(Color.lightGray);
      plot.setRangeGridlinePaint(Color.white);

      // customise the range axis...
      NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

      // customise the renderer...
      LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
      renderer.setShapesVisible(true);
      renderer.setDrawOutlines(true);
      renderer.setUseFillPaint(true);
      renderer.setFillPaint(Color.white);

      return chart;

   }

   private JFreeChart createChart(final XYDataset dataset) {
      final JFreeChart result = ChartFactory.createTimeSeriesChart("Original Graph", "Time", "Value", dataset, true,
            true, false);

      final XYPlot plot = result.getXYPlot();

      plot.setBackgroundPaint(new Color(0xffffe0));
      plot.setDomainGridlinesVisible(true);
      plot.setDomainGridlinePaint(Color.lightGray);
      plot.setRangeGridlinesVisible(true);
      plot.setRangeGridlinePaint(Color.lightGray);

      ValueAxis xaxis = plot.getDomainAxis();
      xaxis.setAutoRange(true);

      // Domain axis would show data of 60 seconds for a time
      xaxis.setFixedAutoRange(150000.0); // 60 seconds
      xaxis.setVerticalTickLabels(true);

      ValueAxis yaxis = plot.getRangeAxis();
      yaxis.setRange(-1.0, 130.0);
      return result;
   }

   public void actionPerformed(final ActionEvent e) {
      if (e.getSource().equals(b1)) {
         System.out.println("���� ��ư�� ���Ƚ��ϴ�.");
         calculator = new Calculator();
         fft = new FFT(samplerate);
         autocorrelation = new autocorrelation();

         try {
            final AudioFormat format = getFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            final TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            running = true;

            Runnable runner = new Runnable() {
               int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
               byte buffer[] = new byte[bufferSize];
               int intArray[] = new int[bufferSize];
               double doubleArray[] = new double[bufferSize];
               int startCheck = -1;
               int check = 0;
               int sleepApnea = 0;
               int hours = 0;
               int min = 0;
               int sec = 0;
               int first = 0;
               int fft_x_count = 0;
               int dft_x_count = 0;

               @SuppressWarnings("deprecation")
               public void run() {
                  out = new ByteArrayOutputStream();
                  try {
                     while (running) {
                        int count = line.read(buffer, 0, buffer.length);

                        if (count > 0) { // 1�ʿ� �� �� ����Ǵ� if�� (sampling data)�� �����Ͱ� Arraylist�� ��.
                           byteArrayToCasting toCasting = new byteArrayToCasting();
                           intArray = toCasting.changeInt(buffer);
                           doubleArray = toCasting.changeDouble(buffer);

                           first += 1;
                           for (int i = 0; i < intArray.length; i++) {

                              before_dft_data.add(intArray[i]);

                           }
                           Value = calculator.pick(before_dft_data); // �Է½�ȣ �׷��� �Է°�
                           series.addOrUpdate(new Second(), Value); // �Է½�ȣ �׷��� ������ �Է� �ϴ� �κ�

                           before_dft_data = calculator.init_set(before_dft_data);
                           before_fft_data = calculator.init_set(doubleArray);

                           after_dft_data = dft.dft(before_dft_data);
                           dft_la.setText("dft ���� �ð� : " + dft.dft_runTime() + "��");
                           after_fft_data = fft.fft(before_fft_data);
                           fft_la.setText("fft ���� �ð� : " + fft.fft_runTime() + "��");
                           after_auco_data = autocorrelation.auco(buffer);
                           auco_la.setText("autocorrelation ���� �ð� : " + autocorrelation.auco_runTime() + "��");

                           before_dft_data.clear();
                           dft_frq = 0;
                           fft_frq = 0;
                           for (int i = 0; i < buffer.length; i++) {

                              dft_dataset.addValue(after_dft_data.get(i), "Magnitudes", i + "");
                              fft_dataset.addValue(after_fft_data.get(i), "Magnitudes", i + "");
                              
                              if (i <= 30) {
                                 // fft_x_count�� 5�̻� ���Ҹ�, ������ ��ȣ��
                                 if (after_fft_data.get(i) != 0) {
                                    fft_x_count += 1;
                                 }
                                 
                                 if (after_dft_data.get(i) != 0) {
                                    dft_x_count += 1;
                                 }
                              }

                              if (i < samplerate / 2 && after_dft_data.get(i) != 0) {
                                 if (i <= 10) {
                                    dft_frq = i;
                                 }
                              }
                              if (i < samplerate / 2 && after_fft_data.get(i) != 0) {
                                 if (i <= 10) {
                                    fft_frq = i;
                                 }
                              }
                           }

                           for (int i = 0; i < after_auco_data.size(); i++) {
                              auco_dataset.addValue(after_auco_data.get(i), "Magnitudes", i + "");
                           }
                           //System.out.println(autocorrelation.auco_max());
                           if (first == 3) {
                              aucoFirMax = autocorrelation.auco_max();
                              System.out.println(aucoFirMax);
                           }
                           aucoMax = autocorrelation.auco_max();

                           after_dft_data.clear();
                           after_auco_data.clear();
                           after_fft_data.clear();

                           if (fft_x_count < 5) {
                              fft_frq = 0;
                           }
                           
                           if (dft_x_count < 5) {
                              dft_frq = 0;
                           }
                           
                           // ���鹫ȣ���� �Ͼ Ƚ�� ���
                           // �������� ���� �� ������ ���� check++
                           Date StartTime = new Date();
                           if (fft_frq == 0 && dft_frq == 0) { //�����Ҹ�(������)����, ��ȣ���ϋ�
                              if (startCheck == -1) {
                                 startCheck = StartTime.getSeconds();
                              } else {
                                 if (startCheck == 59 && StartTime.getSeconds() - 1 == -1) {
                                    check++;
                                    startCheck = StartTime.getSeconds();
                                 } else {
                                    if (StartTime.getSeconds() - 1 == startCheck) {
                                       check++;
                                       startCheck = StartTime.getSeconds();
                                    }
                                 }
                              }
                           }
                           else if(aucoMax < aucoFirMax+500) {
                              if (startCheck == -1) {
                                   startCheck = StartTime.getSeconds();
                                } else {
                                   if (startCheck == 59 && StartTime.getSeconds() - 1 == -1) {
                                      check++;
                                      startCheck = StartTime.getSeconds();
                                   } else {
                                      if (StartTime.getSeconds() - 1 == startCheck) {
                                         check++;
                                         startCheck = StartTime.getSeconds();
                                     }
                                   }
                                 }
                              }
                           else {
                              startCheck = -1;
                              check = 0;
                           }
                           if (check % 20 == 0 && check != 0) { // 20�ʵ��� ���� �� ������ �� ��ȣ��++
                              sleepApnea++;
                              // ó�� ��ȣ���� ���� ����
                              // �Ǵ� 1�ð��� ������ �� �ڿ� ���� -> �ð� �� Ƚ���� ��ȣ���� ������ �Ǵ�
                              if (sleepApnea == 1) {
                                 System.out.println("��ȣ���� ���� �ð��� " + StartTime.getHours() + "�� "
                                       + StartTime.getMinutes() + "�� " + StartTime.getSeconds()
                                       + "�� �Դϴ�.");
                                 hours = StartTime.getHours();
                                 min = StartTime.getMinutes();
                                 sec = StartTime.getSeconds();
                              }
                              label.setText("���鹫ȣ�� Ƚ�� : " + sleepApnea);

                              if (hours + 1 > StartTime.getHours()) {
                                 if (sleepApnea > 0 && sleepApnea < 5) {

                                 } else if (sleepApnea >= 5 && sleepApnea < 15) {
                                    label2.setText("���鹫ȣ�� ���� : ���� �����Դϴ�.");
                                 } else if (sleepApnea >= 15 && sleepApnea < 30) {
                                    label2.setText("���鹫ȣ�� ���� : �߰� �����Դϴ�.");
                                 } else {
                                    label2.setText("���鹫ȣ�� ���� : ���� �����Դϴ�.");
                                 }
                              } else if (hours + 1 == StartTime.getHours() && min >= StartTime.getMinutes()) {
                                 if (sleepApnea > 0 && sleepApnea < 5) {

                                 } else if (sleepApnea >= 5 && sleepApnea < 15) {
                                    label2.setText("���鹫ȣ�� ���� : ���� �����Դϴ�.");
                                 } else if (sleepApnea >= 15 && sleepApnea < 30) {
                                    label2.setText("���鹫ȣ�� ���� : �߰� �����Դϴ�.");
                                 } else {
                                    label2.setText("���鹫ȣ�� ���� : ���� �����Դϴ�.");
                                 }
                              } else if (hours + 1 == StartTime.getHours() && min == StartTime.getMinutes()
                                    && sec >= StartTime.getSeconds()) {
                                 if (sleepApnea > 0 && sleepApnea < 5) {

                                 } else if (sleepApnea >= 5 && sleepApnea < 15) {
                                    label2.setText("���鹫ȣ�� ���� : ���� �����Դϴ�.");
                                 } else if (sleepApnea >= 15 && sleepApnea < 30) {
                                    label2.setText("���鹫ȣ�� ���� : �߰� �����Դϴ�.");
                                 } else {
                                    label2.setText("���鹫ȣ�� ���� : ���� �����Դϴ�.");
                                 }
                              } else {
                                 sleepApnea = 0;
                                 label2.setText("���鹫ȣ�� ���� : ����");
                              }
                              map.put(StartTime.getMinutes() + "�� " + StartTime.getSeconds() + "��",
                                    "" + hours);
                           }

                        }
                     }
                     out.close();

                  } catch (IOException e) {
                     System.err.println("I/O problems: " + e);
                     System.exit(-1);
                  }
               }
            };
            Thread captureThread = new Thread(runner);
            captureThread.start();
         } catch (LineUnavailableException q) {
            System.err.println("Line unavailable: " + q);
            System.exit(-2);
         }

      } else {
         System.out.println("���� ��ư�� ���Ƚ��ϴ�.");

         Iterator<String> keySetIterator = map.keySet().iterator();

         while (keySetIterator.hasNext()) {
            String key = keySetIterator.next();

            System.out.println("��ȣ�� �� �ð� : " + map.get(key) + "�� " + key);
         }
      }
   }

   public AudioFormat getFormat() {
      float sampleRate = samplerate;
      int sampleSizeInBits = 8;
      int channels = 1;
      boolean signed = true;
      boolean bigEndian = true;
      return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
   }

   public void paint(Graphics g) {
      g.drawLine(0, 0, 0, height);
      g.drawLine(0, 0, width, 0);
      g.drawLine(0, height, width, height);
      g.drawLine(width, 0, width, height);
   }

   public static void main(String[] args) {
      // TODO Auto-generated method stub
      final ExamJfree chart = new ExamJfree();
      chart.pack();
      RefineryUtilities.centerFrameOnScreen(chart);
      chart.setVisible(true);
   }

}