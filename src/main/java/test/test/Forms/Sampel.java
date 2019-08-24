/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.test.Forms;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.DBException;
import org.javalite.activejdbc.LazyList;
import test.test.Models.JenisPengujianModel;
import test.test.Models.KaryawanModel;
import test.test.Models.KodeSampelModel;
import test.test.Models.SampelModel;

import test.test.Models.TempatTugasModel;
import test.test.Reports.Config;

/**
 *
 * @author user
 */
public class Sampel extends javax.swing.JFrame {
    private List<Integer> comboKodeID = new ArrayList<Integer>();
    private int comboKodeIndex;
    private int selectedComboKodeIndex;
    
//    private List<Integer> comboJenisUjiID = new ArrayList<Integer>();
//    private int comboJenisUjiIndex;
//    private int selectedComboJenisUjiIndex;
    
    private DefaultTableModel model = new DefaultTableModel();
    private String ID;
    private String state;
    /**
     * Creates new form PangkatGol
     */
    public Sampel() {
        initComponents();
                
        loadTable();
                
        TextCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                cari();
            }

            public void removeUpdate(DocumentEvent e) {
                cari();
            }

            public void changedUpdate(DocumentEvent e) {
                cari();
            }
        });
        
        loadComboBox();
        
        selectKode();
        
        Kode.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                selectKode();
            }
        });
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    
    public void selectKode() {
        comboKodeIndex = Kode.getSelectedIndex();
        if (comboKodeIndex >= 0) {
            selectedComboKodeIndex = comboKodeID.get(comboKodeIndex);

            Base.open();
            KodeSampelModel p = KodeSampelModel.findById(selectedComboKodeIndex);
            Base.close();

            Jenis.setText(p.getString("jenis_sampel"));
        }
    }
    
    public void loadComboBox() {
//        JenisUji.removeAllItems();
        Kode.removeAllItems();
        
        Base.open();
//        LazyList<JenisPengujianModel> jenisUjis = JenisPengujianModel.findAll();
        LazyList<KodeSampelModel> kodeSampels = KodeSampelModel.findAll();
        
//        for(JenisPengujianModel jenisUji : jenisUjis) {
//            comboJenisUjiID.add(Integer.parseInt(jenisUji.getString("id")));
//            JenisUji.addItem(jenisUji.getString("nama_jenis_pengujian"));
//        }

        for(KodeSampelModel kodeSampel : kodeSampels) {
            comboKodeID.add(Integer.parseInt(kodeSampel.getString("id")));
            Kode.addItem(kodeSampel.getString("kode"));
        }

        Base.close();
    }
    
    public void cari() {
        if (TextCari.getText().equals("")) {
            loadTable();
        } else {
            loadTable(TextCari.getText());
        }
    }
    
    private void loadTableHelper(LazyList<SampelModel> sampels) {
        model = new DefaultTableModel();
                
        model.addColumn("#ID");
        model.addColumn("No Formulir");
        model.addColumn("Nama Sampel");
        model.addColumn("Kode Sampel");
        model.addColumn("Jenis Sampel");
        model.addColumn("Alamat");
        model.addColumn("Kecamatan");
        model.addColumn("Tanggal");
        model.addColumn("Jumlah");
        model.addColumn("Kondisi");
//        model.addColumn("Jenis Pengujian");
       
        Base.open();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            for(SampelModel sampel : sampels) {               
                Date tanggal = format.parse(sampel.getString("tanggal_sampel"));
                SimpleDateFormat parsedFormat = new SimpleDateFormat("dd-MM-YYYY");
                String parsedtanggal = parsedFormat.format(tanggal);
                
                KodeSampelModel kodeSampel = sampel.parent(KodeSampelModel.class);
//                JenisPengujianModel jenisPengujian = sampel.parent(JenisPengujianModel.class);
                model.addRow(new Object[]{
                    sampel.getId(),
                    sampel.getString("no_formulir"),
                    sampel.getString("nama"),
                    kodeSampel.getString("kode") + sampel.getString("no_kode"),
                    kodeSampel.getString("jenis_sampel"),
                    sampel.getString("alamat"),
                    sampel.getString("kecamatan"),
                    parsedtanggal,
                    sampel.getString("jumlah"),
                    sampel.getString("kondisi"),
//                    jenisPengujian.getString("nama_jenis_pengujian"),
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        Base.close();
        
        TablePegawai.setModel(model);
        
        setState("index");
    }
    
    private void formatTextInteger() {
       
    }
    
    private void loadTable() {
        Base.open();
        LazyList<SampelModel> sampels = SampelModel.findAll();
        Base.close();
        
        loadTableHelper(sampels);
    }

    private void loadTable(String cari) {
        Base.open();
        LazyList<SampelModel> sampels = SampelModel.where("no_formulir like ?", '%' + cari + '%');
        Base.close();
        
        loadTableHelper(sampels);
    }

    
    private void hapusData() {
        Base.open();
        SampelModel sampel = SampelModel.findById(ID);
        try {
            sampel.delete();
        } catch (DBException e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
        Base.close();
    }
    
    private void setState(String IndexOrEdit) {
        if (IndexOrEdit.equals("index")) {
            ButtonTambahUbah.setText("Tambah");
            ButtonResetHapus.setText("Reset");
            
            state = IndexOrEdit;
        } else if (IndexOrEdit.equals("edit")) {
            ButtonTambahUbah.setText("Ubah");
            ButtonResetHapus.setText("Hapus");
            
            state = IndexOrEdit;
        } else {
            JOptionPane.showMessageDialog(null, "Index/Edit ?");
        }
    }
    
    private void tambahData() {
        Base.open();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            SampelModel sampel = new SampelModel();
            sampel.set("id_kode_sampel", selectedComboKodeIndex);
//            sampel.set("id_jenis_pengujian", selectedComboJenisUjiIndex);
            sampel.set("no_formulir", No.getText());
            sampel.set("nama", Nama.getText());
            sampel.set("alamat", Alamat.getText());
            sampel.set("kecamatan", Kecamatan.getSelectedItem());
            sampel.set("tanggal_sampel", dateFormat.format(Tanggal.getDate()));
            sampel.set("jumlah", Jumlah.getValue());
            sampel.set("no_kode", NoKode.getValue());
            if (Baik.isSelected()) {
                sampel.set("kondisi", "Baik");   
            } else {
                sampel.set("kondisi", "Kurang Baik");
            }
            sampel.save();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        Base.close();
    }
    
    private void ubahData() {
        Base.open();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            SampelModel sampel = SampelModel.findById(ID);
            sampel.set("id_kode_sampel", selectedComboKodeIndex);
//            sampel.set("id_jenis_pengujian", selectedComboJenisUjiIndex);
            sampel.set("no_formulir", No.getText());
            sampel.set("nama", Nama.getText());
            sampel.set("alamat", Alamat.getText());
            sampel.set("kecamatan", Kecamatan.getSelectedItem());
            sampel.set("tanggal_sampel", dateFormat.format(Tanggal.getDate()));
            sampel.set("jumlah", Jumlah.getValue());
            sampel.set("no_kode", NoKode.getValue());
            if (Baik.isSelected()) {
                sampel.set("kondisi", "Baik");   
            } else {
                sampel.set("kondisi", "Kurang Baik");
            }
            sampel.save();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        Base.close();
    }

    private void resetForm() {
        Kode.setSelectedIndex(0);
        Jenis.setText("");
        No.setText("");
        Nama.setText("");
        Alamat.setText("");
        Kecamatan.setSelectedIndex(0);
        Tanggal.setDate(null);
        Jumlah.setValue(0);
        NoKode.setValue(0);
        Baik.setSelected(false);
        Kurang.setSelected(false);
//        JenisUji.setSelectedIndex(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ButtonRefresh = new javax.swing.JButton();
        ScrollPane = new javax.swing.JScrollPane();
        TablePegawai = new javax.swing.JTable();
        ButtonTambahUbah = new javax.swing.JButton();
        ButtonResetHapus = new javax.swing.JButton();
        TextCari = new javax.swing.JTextField();
        LabelCari = new javax.swing.JLabel();
        LabelCari1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        Jenis = new javax.swing.JTextField();
        LabelCari2 = new javax.swing.JLabel();
        Kode = new javax.swing.JComboBox<>();
        No = new javax.swing.JTextField();
        LabelCari4 = new javax.swing.JLabel();
        Alamat = new javax.swing.JTextField();
        LabelCari5 = new javax.swing.JLabel();
        LabelCari6 = new javax.swing.JLabel();
        LabelCari7 = new javax.swing.JLabel();
        Nama = new javax.swing.JTextField();
        Kecamatan = new javax.swing.JComboBox<>();
        LabelCari8 = new javax.swing.JLabel();
        LabelCari9 = new javax.swing.JLabel();
        Tanggal = new com.toedter.calendar.JDateChooser();
        LabelCari10 = new javax.swing.JLabel();
        Baik = new javax.swing.JRadioButton();
        Kurang = new javax.swing.JRadioButton();
        Jumlah = new javax.swing.JSpinner();
        LabelCari11 = new javax.swing.JLabel();
        NoKode = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sampel");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        ButtonRefresh.setText("Refresh");
        ButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonRefreshActionPerformed(evt);
            }
        });

        TablePegawai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        TablePegawai.getTableHeader().setReorderingAllowed(false);
        TablePegawai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablePegawaiMouseClicked(evt);
            }
        });
        ScrollPane.setViewportView(TablePegawai);

        ButtonTambahUbah.setText("Tambah");
        ButtonTambahUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonTambahUbahActionPerformed(evt);
            }
        });

        ButtonResetHapus.setText("Reset");
        ButtonResetHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonResetHapusActionPerformed(evt);
            }
        });

        TextCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextCariActionPerformed(evt);
            }
        });

        LabelCari.setText("Cari (No Formulir)");

        LabelCari1.setText("Kode");

        jLabel4.setText("RBH DAN LABORATORIUM KESEHATAN MASYARAKAT VETERINER (KESMAVET)");

        jLabel5.setText("DINAS KETAHANAN PANGAN PERTANIAN DAN PERIKANAN KOTA METRO");

        jLabel6.setText("Jalan Macan No. 22 Hadimulyo Timur Metro Pusat, Kota Metro. Kode Pos 34113");

        Jenis.setEditable(false);
        Jenis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JenisActionPerformed(evt);
            }
        });

        LabelCari2.setText("Jenis Sampel");

        Kode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        Kode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KodeActionPerformed(evt);
            }
        });

        No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoActionPerformed(evt);
            }
        });

        LabelCari4.setText("No Formuilir");

        Alamat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AlamatActionPerformed(evt);
            }
        });

        LabelCari5.setText("Alamat");

        LabelCari6.setText("Jumlah");

        LabelCari7.setText("Nama");

        Nama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NamaActionPerformed(evt);
            }
        });

        Kecamatan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Metro Pusat", "Metro Timur", "Metro Utara", "Metro Barat", "Metro Selatan" }));

        LabelCari8.setText("Kecamatan");

        LabelCari9.setText("Tanggal");

        Tanggal.setDateFormatString("dd-MM-yyyy");

        LabelCari10.setText("Kondisi");

        Baik.setText("Baik");
        Baik.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BaikActionPerformed(evt);
            }
        });

        Kurang.setText("Kurang Baik");
        Kurang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KurangActionPerformed(evt);
            }
        });

        LabelCari11.setText("No Kode");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel4)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel5))))
                .addGap(182, 182, 182))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollPane)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(191, 191, 191)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ButtonTambahUbah)
                        .addGap(29, 29, 29)
                        .addComponent(ButtonRefresh)
                        .addGap(28, 28, 28)
                        .addComponent(ButtonResetHapus)
                        .addGap(56, 56, 56))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(LabelCari)
                        .addGap(18, 18, 18)
                        .addComponent(TextCari, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(211, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(LabelCari7)
                        .addGap(18, 18, 18)
                        .addComponent(Nama, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LabelCari1)
                                .addGap(18, 18, 18)
                                .addComponent(Kode, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LabelCari4)
                                .addGap(18, 18, 18)
                                .addComponent(No, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LabelCari2)
                                .addGap(18, 18, 18)
                                .addComponent(Jenis, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LabelCari11)
                                .addGap(18, 18, 18)
                                .addComponent(NoKode, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(LabelCari9)
                        .addGap(18, 18, 18)
                        .addComponent(Tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(LabelCari10)
                        .addGap(18, 18, 18)
                        .addComponent(Baik)
                        .addGap(51, 51, 51)
                        .addComponent(Kurang)
                        .addGap(37, 37, 37))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(LabelCari8)
                        .addGap(18, 18, 18)
                        .addComponent(Kecamatan, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(LabelCari6)
                        .addGap(18, 18, 18)
                        .addComponent(Jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(LabelCari5)
                        .addGap(18, 18, 18)
                        .addComponent(Alamat, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(81, 81, 81))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelCari1)
                    .addComponent(Kode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelCari5)
                    .addComponent(Alamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari11)
                            .addComponent(NoKode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari2)
                            .addComponent(Jenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari4)
                            .addComponent(No, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari7)
                            .addComponent(Nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari8)
                            .addComponent(Kecamatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(LabelCari9)
                            .addComponent(Tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari6)
                            .addComponent(Jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari10)
                            .addComponent(Baik)
                            .addComponent(Kurang))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonRefresh)
                    .addComponent(ButtonTambahUbah)
                    .addComponent(ButtonResetHapus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TextCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelCari))
                .addGap(18, 18, 18)
                .addComponent(ScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonRefreshActionPerformed
        resetForm();
        loadTable();
        TextCari.setText("");
    }//GEN-LAST:event_ButtonRefreshActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        new MenuUtama().setVisible(true);
        
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void TablePegawaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablePegawaiMouseClicked
        int i =TablePegawai.getSelectedRow();
        if(i>=0){
            ID = model.getValueAt(i, 0).toString();

            Base.open();
            SampelModel sampel = SampelModel.findById(ID);
            Base.close();
            
            Kode.setSelectedIndex(comboKodeID.indexOf(Integer.parseInt(sampel.getString("id_kode_sampel"))));
//            JenisUji.setSelectedIndex(comboJenisUjiID.indexOf(Integer.parseInt(sampel.getString("id_jenis_pengujian"))));
            No.setText(sampel.getString("no_formulir"));
            Nama.setText(sampel.getString("nama"));
            Alamat.setText(sampel.getString("alamat"));
            Kecamatan.setSelectedItem(sampel.getString("kecamatan"));
            Jumlah.setValue(Integer.parseInt(sampel.getString("jumlah")));
            NoKode.setValue(Integer.parseInt(sampel.getString("no_kode")));
            
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Tanggal.setDate(format.parse(sampel.getString("tanggal_sampel")));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
            
            if (sampel.getString("kondisi").equals("Baik")) {
                Baik.setSelected(true);
                Kurang.setSelected(false); 
            } else {
                Kurang.setSelected(true); 
                Baik.setSelected(false);
            }
            
            setState("edit");
        }
    }//GEN-LAST:event_TablePegawaiMouseClicked

    private void ButtonTambahUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonTambahUbahActionPerformed
        if (state.equals("index")) {
            if (No.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Form No Formulir Masih Kosong !!!");
            } else if (Nama.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Form Nama Masih Kosong !!!");
            } else if (Alamat.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Form Alamat Masih Kosong !!!");
            } else if (Tanggal.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Form Tanggal Masih Kosong !!!");
            } else if (!Baik.isSelected() && !Kurang.isSelected()) {
                JOptionPane.showMessageDialog(null, "Form Kondisi Belum Dipilih !!!");
            } else {
                tambahData();
                resetForm();
                loadTable();
            }
        } else {
            if (No.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Form No Formulir Masih Kosong !!!");
            } else if (Nama.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Form Nama Masih Kosong !!!");
            } else if (Alamat.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Form Alamat Masih Kosong !!!");
            } else if (Tanggal.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Form Tanggal Masih Kosong !!!");
            } else if (!Baik.isSelected() && !Kurang.isSelected()) {
                JOptionPane.showMessageDialog(null, "Form Kondisi Belum Dipilih !!!");
            } else {
                ubahData();
                resetForm();
                loadTable();
            }
        }
    }//GEN-LAST:event_ButtonTambahUbahActionPerformed

    private void ButtonResetHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonResetHapusActionPerformed
        if (state.equals("edit")) {
            hapusData();
            loadTable();
        }

        resetForm();
    }//GEN-LAST:event_ButtonResetHapusActionPerformed

    private void TextCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextCariActionPerformed
        cari();
    }//GEN-LAST:event_TextCariActionPerformed

    private void JenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JenisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JenisActionPerformed

    private void NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoActionPerformed

    private void AlamatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AlamatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AlamatActionPerformed

    private void NamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NamaActionPerformed

    private void BaikActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BaikActionPerformed
        Kurang.setSelected(false);
    }//GEN-LAST:event_BaikActionPerformed

    private void KurangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KurangActionPerformed
        Baik.setSelected(false);
    }//GEN-LAST:event_KurangActionPerformed

    private void KodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KodeActionPerformed
        comboKodeIndex = Kode.getSelectedIndex();
        if (comboKodeIndex >= 0) {
            selectedComboKodeIndex = comboKodeID.get(comboKodeIndex);
        }
    }//GEN-LAST:event_KodeActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sampel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sampel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sampel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sampel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Sampel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Alamat;
    private javax.swing.JRadioButton Baik;
    private javax.swing.JButton ButtonRefresh;
    private javax.swing.JButton ButtonResetHapus;
    private javax.swing.JButton ButtonTambahUbah;
    private javax.swing.JTextField Jenis;
    private javax.swing.JSpinner Jumlah;
    private javax.swing.JComboBox<String> Kecamatan;
    private javax.swing.JComboBox<String> Kode;
    private javax.swing.JRadioButton Kurang;
    private javax.swing.JLabel LabelCari;
    private javax.swing.JLabel LabelCari1;
    private javax.swing.JLabel LabelCari10;
    private javax.swing.JLabel LabelCari11;
    private javax.swing.JLabel LabelCari2;
    private javax.swing.JLabel LabelCari4;
    private javax.swing.JLabel LabelCari5;
    private javax.swing.JLabel LabelCari6;
    private javax.swing.JLabel LabelCari7;
    private javax.swing.JLabel LabelCari8;
    private javax.swing.JLabel LabelCari9;
    private javax.swing.JTextField Nama;
    private javax.swing.JTextField No;
    private javax.swing.JSpinner NoKode;
    private javax.swing.JScrollPane ScrollPane;
    private javax.swing.JTable TablePegawai;
    private com.toedter.calendar.JDateChooser Tanggal;
    private javax.swing.JTextField TextCari;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
