/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import javax.swing.table.DefaultTableModel;
import utils.Auth;
import DAO.ThongKeDAO;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.ImageIcon;
import utils.XImage;
import DAO.ChuyenDeDAO;
import Entity.ChuyenDe;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import DAO.KhoaHocDAO;
import Entity.KhoaHoc;
import utils.XDate;

/**
 *
 * @author ACER
 */
public class DoanhThuJDialog extends javax.swing.JDialog {

    /**
     * Creates new form DoanhThuJDialog
     */
    DefaultTableModel tab1;
    DefaultTableModel tab2;
    DefaultTableModel tab3;
    DefaultTableModel tab4;
    DefaultComboBoxModel cb1;
    DefaultComboBoxModel cb2;

    public DoanhThuJDialog(java.awt.Frame parent, boolean modal, int tab) {
        super(parent, modal);
        initComponents();
        init(parent, tab);
        fill_table1();
        fill_table2();
        fill_table3();
        fill_table4();
    }

    public void init(java.awt.Frame parent, int tab) {
        setTitle("EduSys - Tổng hợp và doanh thu");
        jTabbedPane1.setSelectedIndex(tab);
        if (!Auth.isManager()) {
            jTabbedPane1.setEnabledAt(3, false);
        }
        setIconImage(XImage.getAppIcon());
        init_cb1();
        init_cb2();
        init_table1();
        init_table2();
        init_table3();
        init_table4();
        setLocationRelativeTo(parent);
    }

    public void init_table1() {
        String[] t = {"MaNH", "HoTen", "Diem"};
        tab1 = new DefaultTableModel(t, 0);
        jTable1.setModel(tab1);
        jTable1.setEnabled(false);
    }

    public void init_table2() {
        String[] t = {"Nam", "SoLuong", "DauTien", "CuoiCung"};
        tab2 = new DefaultTableModel(t, 0);
        jTable2.setModel(tab2);
        jTable2.setEnabled(false);
    }

    public void init_table3() {
        String[] t = {"ChuyenDe", "SoHV", "ThapNhat", "CaoNhat", "TrungBinh"};
        tab3 = new DefaultTableModel(t, 0);
        jTable3.setModel(tab3);
        jTable3.setEnabled(false);
    }

    public void init_table4() {
        String[] t = {"ChuyenDe", "SoKH", "SoHV", "DoanhThu", "ThapNhat", "CaoNhat", "TrungBinh"};
        tab4 = new DefaultTableModel(t, 0);
        jTable4.setModel(tab4);
        jTable4.setEnabled(false);
    }

    public void fill_table1() {
        List<Object[]> list = new ThongKeDAO().getBangDiem(Integer.valueOf(jComboBox1.getSelectedItem().toString().split("-", 2)[1]));
        tab1.setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            list.get(i)[2] = Double.valueOf(Math.round(Double.valueOf(list.get(i)[2].toString()) * 10)) / 10;
            tab1.addRow(list.get(i));
        }
    }

    public void fill_table2() {
        List<Object[]> list = new ThongKeDAO().getLuongNguoiHoc();
        tab2.setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            Calendar cl = Calendar.getInstance();
            cl.setTime(XDate.toDate(list.get(i)[2].toString(), "yyyy-MM-dd"));
            list.get(i)[2] = (String.valueOf(cl.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(cl.get(Calendar.MONTH) + 1) + "/" + String.valueOf(cl.get(Calendar.YEAR)));
            cl.setTime(XDate.toDate(list.get(i)[3].toString(), "yyyy-MM-dd"));
            list.get(i)[3] = list.get(i)[3] = (String.valueOf(cl.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(cl.get(Calendar.MONTH) + 1) + "/" + String.valueOf(cl.get(Calendar.YEAR)));
            tab2.addRow(list.get(i));
        }
    }

    public void fill_table3() {
        List<Object[]> list = new ThongKeDAO().getDiemChuyenDe();
        tab3.setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            list.get(i)[2] = Double.valueOf(Math.round(Double.valueOf(list.get(i)[2].toString()) * 10)) / 10;
            list.get(i)[3] = Double.valueOf(Math.round(Double.valueOf(list.get(i)[3].toString()) * 10)) / 10;
            list.get(i)[4] = Double.valueOf(Math.round(Double.valueOf(list.get(i)[4].toString()) * 10)) / 10;
            tab3.addRow(list.get(i));
        }
    }

    public void fill_table4() {
        List<Object[]> list = new ThongKeDAO().getDoanhThu(Integer.valueOf(jComboBox2.getSelectedItem().toString()));
        tab4.setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            tab4.addRow(list.get(i));
        }
    }

    public void init_cb1() {
        List<KhoaHoc> list = new KhoaHocDAO().selectAll();
        Vector v = new Vector();
        for (KhoaHoc d : list) {
            String khoaHoc = d.getMaCD() + "-" + Integer.valueOf(d.getMaKH());
            v.add(khoaHoc);
        }
        cb1 = new DefaultComboBoxModel(v);
        jComboBox1.setModel(cb1);
    }

    public void init_cb2() {
        Vector v = new Vector();
        Calendar cl = Calendar.getInstance();
        cl.setTime(new Date());
        for (int i = 2010; i <= cl.get(Calendar.YEAR) + 3; i++) {
            v.add(i);
        }
        cb2 = new DefaultComboBoxModel(v);
        jComboBox2.setModel(cb2);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        lblTitle.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblTitle.setText("Tổng hợp - Thống kê");

        jLabel2.setText("Khóa học: ");

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Ma NH", "Ho va ten", "Diem", "Xếp loại"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(96, 96, 96))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Bảng điểm", jPanel1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Năm", "Số NH", "Điểm sớm nhất", "Điểm muộn nhất"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Người học", jPanel2);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Chuyên đề", "SLHV", "Điểm TN", "Điểm CN", "Điểm TB"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Điểm chuyên đề", jPanel3);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Chuyên đề", "Số KH", "Số HV", "Doanh thu", "HPCN", "HPTN", "HPTB"
            }
        ));
        jScrollPane4.setViewportView(jTable4);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2018" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jLabel3.setText("Năm");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(158, 158, 158))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(21, 21, 21)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Doanh thu", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        fill_table1();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
        fill_table4();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

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
            java.util.logging.Logger.getLogger(DoanhThuJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DoanhThuJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DoanhThuJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DoanhThuJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DoanhThuJDialog dialog = new DoanhThuJDialog(new javax.swing.JFrame(), true, 0);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JLabel lblTitle;
    // End of variables declaration//GEN-END:variables
}
