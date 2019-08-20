/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.test.Models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

/**
 *
 * @author user
 */
@Table("sampel")
@BelongsToParents({
    @BelongsTo(parent = KodeSampelModel.class, foreignKeyName = "id_kode_sampel"),
    @BelongsTo(parent = JenisPengujianModel.class, foreignKeyName = "id_jenis_pengujian")
})
public class SampelModel extends Model {}