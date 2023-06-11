package ldev.myNotifier.di

//import android.content.Context
//import androidx.room.Room
//import dagger.Module
//import dagger.Provides
//import ldev.myNotifier.data.room.AppDatabase
//import ldev.myNotifier.data.room.NotificationDao
//import javax.inject.Singleton
//
//@Module
//class RoomModule {
//
//    @Provides
//    @Singleton
//    fun provideAppDatabase(context: Context): AppDatabase {
//        return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.databaseName)
//            .build()
//    }
//
//    @Provides
//    fun provideNotificationDao(appDatabase: AppDatabase): NotificationDao {
//        return appDatabase.getNotificationDao()
//    }
//
//}