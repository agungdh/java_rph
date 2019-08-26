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
import test.test.Models.HasilUjiModel;
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
public class Hasil extends javax.swing.JFrame {
    private List<Integer> comboSampelID = new ArrayList<Integer>();
    private int comboSampelIndex;
    private int selectedComboSampelIndex;
    
    private List<Integer> comboJenisUjiID = new ArrayList<Integer>();
    private int comboJenisUjiIndex;
    private int selectedComboJenisUjiIndex;
    
    private DefaultTableModel model = new DefaultTableModel();
    private String ID;
    private String state;
    /**
     * Creates new form PangkatGol
     */
    public Hasil() {
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
        
        Sampel.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                selectKode();
            }
        });
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    
    public void selectKode() {
        comboSampelIndex = Sampel.getSelectedIndex();
        if (comboSampelIndex >= 0) {
            selectedComboSampelIndex = comboSampelID.get(comboSampelIndex);

            Base.open();
            SampelModel p = SampelModel.findById(selectedComboSampelIndex);
            KodeSampelModel kode = p.parent(KodeSampelModel.class);
            Base.close();

            Nama.setText(p.getString("nama"));
            Kode.setText(kode.getString("kode") + p.getString("no_kode"));
            Jenis.setText(kode.getString("jenis_sampel"));
            Formulir.setText(p.getString("no_formulir"));
            
            JenisUji.removeAllItems();
            
            Base.open();
            LazyList<JenisPengujianModel> jenisPengujians = p.getAll(JenisPengujianModel.class);
            
            for(JenisPengujianModel jenisUji : jenisPengujians) {
                comboJenisUjiID.add(Integer.parseInt(jenisUji.getString("id")));
                JenisUji.addItem(jenisUji.getString("nama_jenis_pengujian"));
            }
            Base.close();
        }
    }
    
    public void loadComboBox() {
        JenisUji.removeAllItems();
        Sampel.removeAllItems();
        
        Base.open();
        LazyList<SampelModel> sampels = SampelModel.findAll().orderBy("id desc");

        for(SampelModel sampel : sampels) {
            comboSampelID.add(Integer.parseInt(sampel.getString("id")));
            Sampel.addItem(sampel.getString("id") + "). " + sampel.getString("nama"));
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
    
    private void loadTableHelper(LazyList<HasilUjiModel> hasilUjis) {
        model = new DefaultTableModel();
                
        model.addColumn("#ID");
        model.addColumn("Nama Sampel");
        model.addColumn("Kode Sampel");
        model.addColumn("Jenis Sampel");
        model.addColumn("Jenis Pengujian");
        model.addColumn("Tanggal Uji");
        model.addColumn("Hasil Uji");
        model.addColumn("Keterangan");
       
        Base.open();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            for(HasilUjiModel hasilUji : hasilUjis) {               
                Date tanggal = format.parse(hasilUji.getString("tanggal_uji"));
                SimpleDateFormat parsedFormat = new SimpleDateFormat("dd-MM-YYYY");
                String parsedtanggal = parsedFormat.format(tanggal);
                
                SampelModel sampel = hasilUji.parent(SampelModel.class);
                KodeSampelModel kodeSampel = sampel.parent(KodeSampelModel.class);
                JenisPengujianModel jenisPengujian = hasilUji.parent(JenisPengujianModel.class);
                
                model.addRow(new Object[]{
                    hasilUji.getId(),
                    sampel.getString("nama"),
                    kodeSampel.getString("kode") + sampel.getString("no_kode"),
                    kodeSampel.getString("jenis_sampel"),
                    jenisPengujian.getString("nama_jenis_pengujian"),
                    parsedtanggal,
                    hasilUji.getString("hasil_uji"),
                    hasilUji.getString("keterangan")
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
        LazyList<HasilUjiModel> hasilUjis = HasilUjiModel.findAll().orderBy("id desc");
        Base.close();
        
        loadTableHelper(hasilUjis);
    }

    private void loadTable(String cari) {
        Base.open();
        LazyList<HasilUjiModel> hasilUjis = HasilUjiModel.where("id like ?", '%' + cari + '%').orderBy("id desc");
        Base.close();
        
        loadTableHelper(hasilUjis);
    }

    
    private void hapusData() {
        Base.open();
        HasilUjiModel hasilUji = HasilUjiModel.findById(ID);
        try {
            hasilUji.delete();
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
            HasilUjiModel hasilUji = new HasilUjiModel();
            hasilUji.set("id_sampel", selectedComboSampelIndex);
            hasilUji.set("id_jenis_pengujian", selectedComboJenisUjiIndex);
            hasilUji.set("tanggal_uji", dateFormat.format(Tanggal.getDate()));
            if (Baik.isSelected()) {
                hasilUji.set("keterangan", "Baik");   
            } else {
                hasilUji.set("keterangan", "Kurang Baik");
            }
            hasilUji.set("hasil_uji", Hasil.getText());   
            hasilUji.save();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        Base.close();
    }
    
    private void ubahData() {
        Base.open();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            HasilUjiModel hasilUji = HasilUjiModel.findById(ID);
            hasilUji.set("id_sampel", selectedComboSampelIndex);
            hasilUji.set("id_jenis_pengujian", selectedComboJenisUjiIndex);
            hasilUji.set("tanggal_uji", dateFormat.format(Tanggal.getDate()));
            if (Baik.isSelected()) {
                hasilUji.set("keterangan", "Baik");   
            } else {
                hasilUji.set("keterangan", "Kurang Baik");
            }
            hasilUji.set("hasil_uji", Hasil.getText());   
            hasilUji.save();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        Base.close();
    }

    private void resetForm() {
        Sampel.setSelectedIndex(0);
        Nama.setText("");
        Kode.setText("");
        Jenis.setText("");
        Formulir.setText("");
        Tanggal.setDate(null);
        Baik.setSelected(false);
        Kurang.setSelected(false);
        JenisUji.setSelectedIndex(0);
        Hasil.setText("");
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
        Nama = new javax.swing.JTextField();
        LabelCari2 = new javax.swing.JLabel();
        Sampel = new javax.swing.JComboBox<>();
        JenisUji = new javax.swing.JComboBox<>();
        LabelCari3 = new javax.swing.JLabel();
        Kode = new javax.swing.JTextField();
        LabelCari4 = new javax.swing.JLabel();
        LabelCari9 = new javax.swing.JLabel();
        Tanggal = new com.toedter.calendar.JDateChooser();
        LabelCari10 = new javax.swing.JLabel();
        Baik = new javax.swing.JRadioButton();
        Kurang = new javax.swing.JRadioButton();
        Jenis = new javax.swing.JTextField();
        LabelCari11 = new javax.swing.JLabel();
        Hasil = new javax.swing.JTextField();
        LabelCari12 = new javax.swing.JLabel();
        Formulir = new javax.swing.JTextField();
        LabelCari13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hasil Uji");
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

        LabelCari.setText("Cari (ID)");

        LabelCari1.setText("Sampel");

        jLabel4.setText("RBH DAN LABORATORIUM KESEHATAN MASYARAKAT VETERINER (KESMAVET)");

        jLabel5.setText("DINAS KETAHANAN PANGAN PERTANIAN DAN PERIKANAN KOTA METRO");

        jLabel6.setText("Jalan Macan No. 22 Hadimulyo Timur Metro Pusat, Kota Metro. Kode Pos 34113");

        Nama.setEditable(false);
        Nama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NamaActionPerformed(evt);
            }
        });

        LabelCari2.setText("Nama Sampel");

        Sampel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        Sampel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SampelActionPerformed(evt);
            }
        });

        JenisUji.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        JenisUji.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JenisUjiActionPerformed(evt);
            }
        });

        LabelCari3.setText("Jenis Pengujian");

        Kode.setEditable(false);
        Kode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KodeActionPerformed(evt);
            }
        });

        LabelCari4.setText("Kode Sampel");

        LabelCari9.setText("Tanggal");

        Tanggal.setDateFormatString("dd-MM-yyyy");

        LabelCari10.setText("Keterangan");

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

        Jenis.setEditable(false);
        Jenis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JenisActionPerformed(evt);
            }
        });

        LabelCari11.setText("Jenis Sampel");

        Hasil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HasilActionPerformed(evt);
            }
        });

        LabelCari12.setText("Hasil Uji");

        Formulir.setEditable(false);
        Formulir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FormulirActionPerformed(evt);
            }
        });

        LabelCari13.setText("No Formulir");

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ScrollPane))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LabelCari4)
                                .addGap(18, 18, 18)
                                .addComponent(Kode, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LabelCari2)
                                .addGap(18, 18, 18)
                                .addComponent(Nama, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LabelCari1)
                                .addGap(18, 18, 18)
                                .addComponent(Sampel, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LabelCari11)
                                .addGap(18, 18, 18)
                                .addComponent(Jenis, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LabelCari13)
                                .addGap(18, 18, 18)
                                .addComponent(Formulir, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(56, 56, 56)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(JenisUji, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(LabelCari12)
                                    .addComponent(LabelCari10)
                                    .addComponent(LabelCari9)
                                    .addComponent(LabelCari3))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Hasil, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(Baik)
                                        .addGap(56, 56, 56)
                                        .addComponent(Kurang)))))
                        .addGap(0, 102, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(217, 217, 217)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ButtonTambahUbah)
                        .addGap(29, 29, 29)
                        .addComponent(ButtonRefresh)
                        .addGap(28, 28, 28)
                        .addComponent(ButtonResetHapus)
                        .addGap(51, 51, 51))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(LabelCari)
                        .addGap(18, 18, 18)
                        .addComponent(TextCari, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(Sampel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelCari3)
                    .addComponent(JenisUji, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(LabelCari2)
                        .addComponent(Nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(LabelCari9))
                    .addComponent(Tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari4)
                            .addComponent(Kode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari11)
                            .addComponent(Jenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari13)
                            .addComponent(Formulir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ButtonRefresh)
                            .addComponent(ButtonTambahUbah)
                            .addComponent(ButtonResetHapus))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TextCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelCari))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(LabelCari10)
                                .addComponent(Baik))
                            .addComponent(Kurang))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Hasil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelCari12))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
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
            HasilUjiModel hasilUji = HasilUjiModel.findById(ID);
            Base.close();
            
            Sampel.setSelectedIndex(comboSampelID.indexOf(Integer.parseInt(hasilUji.getString("id_sampel"))));
            JenisUji.setSelectedIndex(comboJenisUjiID.indexOf(Integer.parseInt(hasilUji.getString("id_jenis_pengujian"))));
            Hasil.setText(hasilUji.getString("hasil_uji"));
            
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Tanggal.setDate(format.parse(hasilUji.getString("tanggal_uji")));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
            
            if (hasilUji.getString("keterangan").equals("Baik")) {
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
            if (Tanggal.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Form Tanggal Masih Kosong !!!");
            } else if (!Baik.isSelected() && !Kurang.isSelected()) {
                JOptionPane.showMessageDialog(null, "Form Keterangan Belum Dipilih !!!");
            } else if ((Hasil.getText().trim().equals(""))) {
                JOptionPane.showMessageDialog(null, "Form Hasil Uji Masih Kosong !!!");
            } else {
                tambahData();
                resetForm();
                loadTable();
            }
        } else {
            if (Tanggal.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Form Tanggal Masih Kosong !!!");
            } else if (!Baik.isSelected() && !Kurang.isSelected()) {
                JOptionPane.showMessageDialog(null, "Form Keterangan Belum Dipilih !!!");
            } else if ((Hasil.getText().trim().equals(""))) {
                JOptionPane.showMessageDialog(null, "Form Hasil Uji Masih Kosong !!!");
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

    private void NamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NamaActionPerformed

    private void KodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KodeActionPerformed

    private void BaikActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BaikActionPerformed
        Kurang.setSelected(false);
    }//GEN-LAST:event_BaikActionPerformed

    private void KurangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KurangActionPerformed
        Baik.setSelected(false);
    }//GEN-LAST:event_KurangActionPerformed

    private void SampelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SampelActionPerformed
        comboSampelIndex = Sampel.getSelectedIndex();
        if (comboSampelIndex >= 0) {
            selectedComboSampelIndex = comboSampelID.get(comboSampelIndex);
        }
    }//GEN-LAST:event_SampelActionPerformed

    private void JenisUjiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JenisUjiActionPerformed
        comboJenisUjiIndex = JenisUji.getSelectedIndex();
        if (comboJenisUjiIndex >= 0) {
            selectedComboJenisUjiIndex = comboJenisUjiID.get(comboJenisUjiIndex);
        }
    }//GEN-LAST:event_JenisUjiActionPerformed

    private void JenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JenisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JenisActionPerformed

    private void HasilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HasilActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_HasilActionPerformed

    private void FormulirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FormulirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FormulirActionPerformed

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
            java.util.logging.Logger.getLogger(Hasil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Hasil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Hasil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Hasil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new Hasil().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton Baik;
    private javax.swing.JButton ButtonRefresh;
    private javax.swing.JButton ButtonResetHapus;
    private javax.swing.JButton ButtonTambahUbah;
    private javax.swing.JTextField Formulir;
    private javax.swing.JTextField Hasil;
    private javax.swing.JTextField Jenis;
    private javax.swing.JComboBox<String> JenisUji;
    private javax.swing.JTextField Kode;
    private javax.swing.JRadioButton Kurang;
    private javax.swing.JLabel LabelCari;
    private javax.swing.JLabel LabelCari1;
    private javax.swing.JLabel LabelCari10;
    private javax.swing.JLabel LabelCari11;
    private javax.swing.JLabel LabelCari12;
    private javax.swing.JLabel LabelCari13;
    private javax.swing.JLabel LabelCari2;
    private javax.swing.JLabel LabelCari3;
    private javax.swing.JLabel LabelCari4;
    private javax.swing.JLabel LabelCari9;
    private javax.swing.JTextField Nama;
    private javax.swing.JComboBox<String> Sampel;
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
