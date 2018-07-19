package constraint.com.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Observable<Notes> notesObservable = getNotesObservable();
        Observer<Notes> observer = getNotesObserver();
        notesObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Notes, Notes>() {
                    @Override
                    public Notes apply(Notes notes) throws Exception {
                        notes.setNote(notes.getNote().toUpperCase());
                        return notes;
                    }
                }).subscribeWith(observer);
    }

    private Observable<Notes> getNotesObservable() {
        final List<Notes> notes = prepareNotes();
        return Observable.create(new ObservableOnSubscribe<Notes>() {
            @Override
            public void subscribe(ObservableEmitter<Notes> emitter) throws Exception {
                for (Notes note : notes) {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(note);
                    }
                }

                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            }
        });
    }

    private List<Notes> prepareNotes() {
        List<Notes> notes = new ArrayList<>();
        notes.add(new Notes(1, "buy tooth paste!"));
        notes.add(new Notes(2, "call brother!"));
        notes.add(new Notes(3, "watch narcos tonight!"));
        notes.add(new Notes(4, "pay power bill!"));
        return notes;
    }

    public Observer<Notes> getNotesObserver() {
        return new Observer<Notes>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(Notes notes) {
                Log.d(TAG, notes.getNote());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Completed!!");
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
