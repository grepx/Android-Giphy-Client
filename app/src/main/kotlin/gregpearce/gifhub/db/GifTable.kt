package gregpearce.gifhub.db

class GifTable {
    //  public static Func1<Cursor, List<TodoList>> MAP = new Func1<Cursor, List<TodoList>>() {
    //    @Override public List<TodoList> call(final Cursor cursor) {
    //      try {
    //        List<TodoList> values = new ArrayList<>(cursor.getCount());
    //
    //        while (cursor.moveToNext()) {
    //          long id = Db.getLong(cursor, ID);
    //          String name = Db.getString(cursor, NAME);
    //          boolean archived = Db.getBoolean(cursor, ARCHIVED);
    //          values.add(new AutoValue_TodoList(id, name, archived));
    //        }
    //        return values;
    //      } finally {
    //        cursor.close();
    //      }
    //    }
    //  };

    companion object {
        val NAME = "Gif"
        val COLUMN_ID = "_id"
        val COLUMN_URL = "url"
        val COLUMN_PATH = "path"
        val COLUMN_THUMBNAIL_URL = "thumbnail_url"
        val COLUMN_THUMBNAIL_PATH = "thumbnail_path"
        val COLUMN_FAVOURITED = "favourited"
    }
}