/*
 * This file is generated by jOOQ.
 */
package net.sync.game.database.schema.tables.records;


import net.sync.game.database.schema.tables.Charts;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ChartsRecord extends UpdatableRecordImpl<ChartsRecord> implements Record9<String, Integer, String, String, Integer, String, String, String, String> {

    private static final long serialVersionUID = -336065885;

    /**
     * Setter for <code>charts.id</code>.
     */
    public void setId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>charts.id</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>charts.song_id</code>.
     */
    public void setSongId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>charts.song_id</code>.
     */
    public Integer getSongId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>charts.hash</code>.
     */
    public void setHash(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>charts.hash</code>.
     */
    public String getHash() {
        return (String) get(2);
    }

    /**
     * Setter for <code>charts.difficulty_class</code>.
     */
    public void setDifficultyClass(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>charts.difficulty_class</code>.
     */
    public String getDifficultyClass() {
        return (String) get(3);
    }

    /**
     * Setter for <code>charts.difficulty_meter</code>.
     */
    public void setDifficultyMeter(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>charts.difficulty_meter</code>.
     */
    public Integer getDifficultyMeter() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>charts.display_bpm</code>.
     */
    public void setDisplayBpm(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>charts.display_bpm</code>.
     */
    public String getDisplayBpm() {
        return (String) get(5);
    }

    /**
     * Setter for <code>charts.name</code>.
     */
    public void setName(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>charts.name</code>.
     */
    public String getName() {
        return (String) get(6);
    }

    /**
     * Setter for <code>charts.description</code>.
     */
    public void setDescription(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>charts.description</code>.
     */
    public String getDescription() {
        return (String) get(7);
    }

    /**
     * Setter for <code>charts.credit</code>.
     */
    public void setCredit(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>charts.credit</code>.
     */
    public String getCredit() {
        return (String) get(8);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record9 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row9<String, Integer, String, String, Integer, String, String, String, String> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    @Override
    public Row9<String, Integer, String, String, Integer, String, String, String, String> valuesRow() {
        return (Row9) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return Charts.CHARTS.ID;
    }

    @Override
    public Field<Integer> field2() {
        return Charts.CHARTS.SONG_ID;
    }

    @Override
    public Field<String> field3() {
        return Charts.CHARTS.HASH;
    }

    @Override
    public Field<String> field4() {
        return Charts.CHARTS.DIFFICULTY_CLASS;
    }

    @Override
    public Field<Integer> field5() {
        return Charts.CHARTS.DIFFICULTY_METER;
    }

    @Override
    public Field<String> field6() {
        return Charts.CHARTS.DISPLAY_BPM;
    }

    @Override
    public Field<String> field7() {
        return Charts.CHARTS.NAME;
    }

    @Override
    public Field<String> field8() {
        return Charts.CHARTS.DESCRIPTION;
    }

    @Override
    public Field<String> field9() {
        return Charts.CHARTS.CREDIT;
    }

    @Override
    public String component1() {
        return getId();
    }

    @Override
    public Integer component2() {
        return getSongId();
    }

    @Override
    public String component3() {
        return getHash();
    }

    @Override
    public String component4() {
        return getDifficultyClass();
    }

    @Override
    public Integer component5() {
        return getDifficultyMeter();
    }

    @Override
    public String component6() {
        return getDisplayBpm();
    }

    @Override
    public String component7() {
        return getName();
    }

    @Override
    public String component8() {
        return getDescription();
    }

    @Override
    public String component9() {
        return getCredit();
    }

    @Override
    public String value1() {
        return getId();
    }

    @Override
    public Integer value2() {
        return getSongId();
    }

    @Override
    public String value3() {
        return getHash();
    }

    @Override
    public String value4() {
        return getDifficultyClass();
    }

    @Override
    public Integer value5() {
        return getDifficultyMeter();
    }

    @Override
    public String value6() {
        return getDisplayBpm();
    }

    @Override
    public String value7() {
        return getName();
    }

    @Override
    public String value8() {
        return getDescription();
    }

    @Override
    public String value9() {
        return getCredit();
    }

    @Override
    public ChartsRecord value1(String value) {
        setId(value);
        return this;
    }

    @Override
    public ChartsRecord value2(Integer value) {
        setSongId(value);
        return this;
    }

    @Override
    public ChartsRecord value3(String value) {
        setHash(value);
        return this;
    }

    @Override
    public ChartsRecord value4(String value) {
        setDifficultyClass(value);
        return this;
    }

    @Override
    public ChartsRecord value5(Integer value) {
        setDifficultyMeter(value);
        return this;
    }

    @Override
    public ChartsRecord value6(String value) {
        setDisplayBpm(value);
        return this;
    }

    @Override
    public ChartsRecord value7(String value) {
        setName(value);
        return this;
    }

    @Override
    public ChartsRecord value8(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public ChartsRecord value9(String value) {
        setCredit(value);
        return this;
    }

    @Override
    public ChartsRecord values(String value1, Integer value2, String value3, String value4, Integer value5, String value6, String value7, String value8, String value9) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ChartsRecord
     */
    public ChartsRecord() {
        super(Charts.CHARTS);
    }

    /**
     * Create a detached, initialised ChartsRecord
     */
    public ChartsRecord(String id, Integer songId, String hash, String difficultyClass, Integer difficultyMeter, String displayBpm, String name, String description, String credit) {
        super(Charts.CHARTS);

        set(0, id);
        set(1, songId);
        set(2, hash);
        set(3, difficultyClass);
        set(4, difficultyMeter);
        set(5, displayBpm);
        set(6, name);
        set(7, description);
        set(8, credit);
    }
}
