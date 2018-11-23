package com.example.diogenes.listmedphone.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.diogenes.listmedphone.model.Actor;

import java.util.ArrayList;

public class ActorDB extends SQLiteOpenHelper {

    private static final String TAG = "sql";

    // Nome do banco
    private static final String NOME_BANCO = "medphone.mobile";
    private static final int VERSAO_BANCO = 1;

    public ActorDB(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Criando a Tabela Actor...");
        db.execSQL("CREATE TABLE IF NOT EXISTS actor " +
                "(id integer PRIMARY KEY," +
                "name TEXT, " +
                "createdAt TEXT," +
                " avatar TEXT);");
        Log.d(TAG, "Tabela Actor criada com sucesso.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Caso mude a versão do banco de dados, podemos executar um SQL aqui
    }

    // Insere um novo actor como favorito
    public long inserir(Actor actor) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("id", actor.id);
            values.put("name", actor.name);
            values.put("createdAt", actor.createdAt);
            values.put("avatar", actor.avatar);

            // insert into actor values (...)
            long id = db.insert("actor", "", values);

            Log.v(TAG, "Actor is favorite " + actor.name);
            return id;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            db.close();
        }
    }

    // Consulta a lista com todos os actor
    public ArrayList<Actor> findAll() {
        SQLiteDatabase db = getReadableDatabase();
        try {
            // select * from actor
            Cursor c = db.query("actor", null, null,
                    null, null, null, null, null);

            ArrayList<Actor> actorList = new ArrayList<Actor>();
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                // The Cursor is now set to the right position
                Actor actor = new Actor();

                actor.id = c.getInt(c.getColumnIndex("id"));
                actor.name = c.getString(c.getColumnIndex("name"));
                actor.createdAt = c.getString(c.getColumnIndex("createdAt"));
                actor.avatar = c.getString(c.getColumnIndex("avatar"));

                actorList.add(actor);

            }
            return actorList;
        } finally {
            db.close();
        }
    }

    public Actor findById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Actor actor = new Actor();
        try {
            // select * from actor
            Cursor c = db.rawQuery("SELECT * FROM actor WHERE id = "+ id, null);

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                // The Cursor is now set to the right position


                actor.id = c.getInt(c.getColumnIndex("id"));
                actor.name = c.getString(c.getColumnIndex("name"));
                actor.createdAt = c.getString(c.getColumnIndex("createdAt"));
                actor.avatar = c.getString(c.getColumnIndex("avatar"));
            }

            return actor;
        } finally {
            db.close();
        }
    }

    public void delete(long id) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.delete("actor", "id=" + id, null);
            Log.v(TAG, "Actor deleted successfully");
        } finally {
            db.close();
        }

    }

}
