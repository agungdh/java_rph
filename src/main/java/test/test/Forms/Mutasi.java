/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.test.Forms;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Image;
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

import test.test.Models.TempatTugasModel;
import test.test.Models.KaryawanModel;
import test.test.Models.MutasiModel;
import test.test.Reports.Config;

/**
 *
 * @author user
 */
public class Mutasi extends javax.swing.JFrame {
    private List<Integer> comboKaryawanID = new ArrayList<Integer>();
    private int comboKaryawanIndex;
    private int selectedComboKaryawanIndex;
    
    private List<Integer> comboLamaID = new ArrayList<Integer>();
    private int comboLamaIndex;
    private int selectedComboLamaIndex;

    private List<Integer> comboBaruID = new ArrayList<Integer>();
    private int comboBaruIndex;
    private int selectedComboBaruIndex;
    
    private DefaultTableModel model = new DefaultTableModel();
    private String ID;
    private String state;
    /**
     * Creates new form PangkatGol
     */
    public Mutasi() {
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

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    
    public void loadComboBox() {
        Karyawan.removeAllItems();
        Lama.removeAllItems();
        Baru.removeAllItems();
        
        Base.open();
        LazyList<TempatTugasModel> tempatTugass = TempatTugasModel.findAll();
        LazyList<KaryawanModel> karyawans = KaryawanModel.findAll();
        
        for(TempatTugasModel tempatTugas : tempatTugass) {
            comboBaruID.add(Integer.parseInt(tempatTugas.getString("id")));
            Baru.addItem(tempatTugas.getString("nama_t_tugas"));
            
            comboLamaID.add(Integer.parseInt(tempatTugas.getString("id")));
            Lama.addItem(tempatTugas.getString("nama_t_tugas"));
        }
        
        for(KaryawanModel karyawan : karyawans) {
            comboKaryawanID.add(Integer.parseInt(karyawan.getString("id")));
            Karyawan.addItem(karyawan.getString("nik") + " " + karyawan.getString("nama"));
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
    
    private void loadTableHelper(LazyList<MutasiModel> mutasis) {
        model = new DefaultTableModel();
                
        model.addColumn("#ID");
        model.addColumn("NIK");
        model.addColumn("Nama");
        model.addColumn("T. Tugas Lama");
        model.addColumn("T. Tugas Baru");
        model.addColumn("No Surat");
        model.addColumn("Tanggal");
        
        Base.open();
        
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            
            for(MutasiModel mutasi : mutasis) {                
                Date tanggal = format.parse(mutasi.getString("tanggal"));

                SimpleDateFormat parsedFormat = new SimpleDateFormat("dd-MM-YYYY");
                String parsedtanggal = parsedFormat.format(tanggal);
                
                KaryawanModel karyawan = mutasi.parent(KaryawanModel.class);
                TempatTugasModel tempatTugasLama = TempatTugasModel.findById(mutasi.getString("id_tempat_tugas_lama"));
                TempatTugasModel tempatTugasBaru = TempatTugasModel.findById(mutasi.getString("id_tempat_tugas_baru"));
                
                model.addRow(new Object[]{
                    mutasi.getId(),
                    karyawan.getString("nik"),
                    karyawan.getString("nama"),
                    tempatTugasLama.getString("nama_t_tugas"),
                    tempatTugasBaru.getString("nama_t_tugas"),
                    mutasi.getString("no_surat"),
                    parsedtanggal
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
        LazyList<MutasiModel> mutasi = MutasiModel.findAll();
        Base.close();
        
        loadTableHelper(mutasi);
    }

    private void loadTable(String cari) {
        Base.open();
        LazyList<MutasiModel> mutasi = MutasiModel.where("no_surat like ?", '%' + cari + '%');
        Base.close();
        
        loadTableHelper(mutasi);
    }

    
    private void hapusData() {
        Base.open();
        MutasiModel mutasi = MutasiModel.findById(ID);
        try {
            mutasi.delete();
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
            MutasiModel mutasi = new MutasiModel();
            mutasi.set("no_surat", No.getText());
            mutasi.set("id_karyawan", selectedComboKaryawanIndex);
            mutasi.set("id_tempat_tugas_lama", selectedComboLamaIndex);
            mutasi.set("id_tempat_tugas_baru", selectedComboBaruIndex);
            mutasi.set("tanggal", dateFormat.format(Tanggal.getDate()));
            mutasi.save();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        Base.close();
    }
    
    private void ubahData() {
        Base.open();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            MutasiModel mutasi = MutasiModel.findById(ID);
            mutasi.set("no_surat", No.getText());
            mutasi.set("id_karyawan", selectedComboKaryawanIndex);
            mutasi.set("id_tempat_tugas_lama", selectedComboLamaIndex);
            mutasi.set("id_tempat_tugas_baru", selectedComboBaruIndex);
            mutasi.set("tanggal", dateFormat.format(Tanggal.getDate()));
            mutasi.save();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        Base.close();
    }

    private void resetForm() {
        No.setText("");
        Tanggal.setDate(null);
        Lama.setSelectedIndex(0);
        Baru.setSelectedIndex(0);
        Karyawan.setSelectedIndex(0);
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
        No = new javax.swing.JTextField();
        LabelCari1 = new javax.swing.JLabel();
        LabelCari4 = new javax.swing.JLabel();
        Tanggal = new com.toedter.calendar.JDateChooser();
        Karyawan = new javax.swing.JComboBox<>();
        LabelCari2 = new javax.swing.JLabel();
        LabelCari3 = new javax.swing.JLabel();
        LabelCari5 = new javax.swing.JLabel();
        Lama = new javax.swing.JComboBox<>();
        Baru = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mutasi");
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

        LabelCari.setText("Cari (No Surat)");

        No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoActionPerformed(evt);
            }
        });

        LabelCari1.setText("No Surat");

        LabelCari4.setText("Tanggal");

        Tanggal.setDateFormatString("dd-MM-yyyy");

        Karyawan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        Karyawan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                KaryawanItemStateChanged(evt);
            }
        });
        Karyawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KaryawanActionPerformed(evt);
            }
        });

        LabelCari2.setText("Karyawan");

        LabelCari3.setText("Tempat Tugas Lama");

        LabelCari5.setText("Tempat Tugas Baru");

        Lama.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        Lama.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                LamaItemStateChanged(evt);
            }
        });
        Lama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LamaActionPerformed(evt);
            }
        });

        Baru.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        Baru.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                BaruItemStateChanged(evt);
            }
        });
        Baru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BaruActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ScrollPane)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(123, 123, 123)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(LabelCari5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LabelCari3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                            .addComponent(LabelCari2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Baru, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Lama, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Karyawan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(117, 117, 117))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LabelCari, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(TextCari, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LabelCari1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LabelCari4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(52, 52, 52)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(No, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(110, 110, 110))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ButtonTambahUbah)
                .addGap(29, 29, 29)
                .addComponent(ButtonRefresh)
                .addGap(28, 28, 28)
                .addComponent(ButtonResetHapus)
                .addGap(203, 203, 203))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelCari2)
                    .addComponent(Karyawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LabelCari3)
                    .addComponent(Lama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelCari5)
                    .addComponent(Baru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelCari1)
                    .addComponent(No, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LabelCari4)
                    .addComponent(Tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonRefresh)
                    .addComponent(ButtonTambahUbah)
                    .addComponent(ButtonResetHapus))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TextCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelCari))
                .addGap(18, 18, 18)
                .addComponent(ScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            MutasiModel mutasi = MutasiModel.findById(ID);
            Base.close();

            No.setText(mutasi.getString("no_surat"));
            
            Karyawan.setSelectedIndex(comboKaryawanID.indexOf(Integer.parseInt(mutasi.getString("id_karyawan"))));
            Lama.setSelectedIndex(comboLamaID.indexOf(Integer.parseInt(mutasi.getString("id_tempat_tugas_lama"))));
            Baru.setSelectedIndex(comboBaruID.indexOf(Integer.parseInt(mutasi.getString("id_tempat_tugas_baru"))));
            
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            
            try {
                Tanggal.setDate(format.parse(mutasi.getString("tanggal")));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
            
            setState("edit");
        }
    }//GEN-LAST:event_TablePegawaiMouseClicked

    private void ButtonTambahUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonTambahUbahActionPerformed
        if (state.equals("index")) {
            if (No.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Form No Surat Masih Kosong !!!");
            } else if (Tanggal.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Form Tanggal Masih Kosong !!!");
            } else {
                tambahData();
                resetForm();
                loadTable();
            }
        } else {
             if (No.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Form No Surat Masih Kosong !!!");
            } else if (Tanggal.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Form Tanggal Masih Kosong !!!");
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

    private void NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoActionPerformed

    private void KaryawanItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_KaryawanItemStateChanged

    }//GEN-LAST:event_KaryawanItemStateChanged

    private void KaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KaryawanActionPerformed
        comboKaryawanIndex = Karyawan.getSelectedIndex();
        if (comboKaryawanIndex >= 0) {
            selectedComboKaryawanIndex = comboKaryawanID.get(comboKaryawanIndex);
        }
    }//GEN-LAST:event_KaryawanActionPerformed

    private void LamaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_LamaItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_LamaItemStateChanged

    private void LamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LamaActionPerformed
        comboLamaIndex = Lama.getSelectedIndex();
        if (comboLamaIndex >= 0) {
            selectedComboLamaIndex = comboLamaID.get(comboLamaIndex);
        }
    }//GEN-LAST:event_LamaActionPerformed

    private void BaruItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_BaruItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_BaruItemStateChanged

    private void BaruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BaruActionPerformed
        comboBaruIndex = Baru.getSelectedIndex();
        if (comboBaruIndex >= 0) {
            selectedComboBaruIndex = comboBaruID.get(comboBaruIndex);
        }
    }//GEN-LAST:event_BaruActionPerformed

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
            java.util.logging.Logger.getLogger(Mutasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Mutasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Mutasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Mutasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Mutasi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Baru;
    private javax.swing.JButton ButtonRefresh;
    private javax.swing.JButton ButtonResetHapus;
    private javax.swing.JButton ButtonTambahUbah;
    private javax.swing.JComboBox<String> Karyawan;
    private javax.swing.JLabel LabelCari;
    private javax.swing.JLabel LabelCari1;
    private javax.swing.JLabel LabelCari2;
    private javax.swing.JLabel LabelCari3;
    private javax.swing.JLabel LabelCari4;
    private javax.swing.JLabel LabelCari5;
    private javax.swing.JComboBox<String> Lama;
    private javax.swing.JTextField No;
    private javax.swing.JScrollPane ScrollPane;
    private javax.swing.JTable TablePegawai;
    private com.toedter.calendar.JDateChooser Tanggal;
    private javax.swing.JTextField TextCari;
    // End of variables declaration//GEN-END:variables
}
