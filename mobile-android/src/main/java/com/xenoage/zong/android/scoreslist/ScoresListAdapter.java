package com.xenoage.zong.android.scoreslist;

import com.xenoage.zong.android.model.Document;
import com.xenoage.zong.android.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ScoresListAdapter
	implements ListAdapter {

	Context context;

	//TODO: support more than these predefined scores
	Document[] documents = {
		new Document("An die ferne Geliebte", "BeetAnGeSample.xml"),
		new Document("Après un rêve", "FaurReveSample.xml"),
		new Document("Dies Irae", "Moza-Dies.xml"),
		new Document("Die zwei blauen Augen", "MahlFaGe4Sample.xml"),
		new Document("Largo B.109 (Chopin)", "Largo.xml"),
		new Document("Locus iste", "Locus iste.xml"),
		new Document("Maria durch ein Dornwald ging", "MariaDornwald.xml"),
		new Document("March of the Wooden Soldiers", "MarchWoodSoldiers.xml"),
		//new Document("Piano Sonata No. 14 (Moonlight Sonata) M1", "Sonata14_1.xml"),
		//new Document("Piano Sonata No. 14 (Moonlight Sonata) M2", "Sonata14_2.xml"),
		//new Document("Piano Sonata No. 14 (Moonlight Sonata) M3", "Sonata14_3.xml"),
		new Document("Polka vom Kaufmann Schorsch", "Kaufmann.xml"),
		new Document("Sind surmani", "Sind surmani.xml"),
		//new Document("Tuljak", "Tuljak.xml"),
		//new Document("Ungarische Tänze Nr. 5", "Brahms.xml"),
		new Document("Topporzer Kreuzpolka", "Topporzer.xml"), new Document("Woaf", "Woaf.xml") };


	public ScoresListAdapter(Context context) {
		this.context = context;
	}

	@Override public View getView(int position, View convertView, ViewGroup parent) {
		Document document = getItem(position);
		View view = LayoutInflater.from(context).inflate(R.layout.listitem, null);
		((ImageView) view.findViewById(R.id.icon)).setImageDrawable(context.getResources().getDrawable(
			R.drawable.icon_score));
		((TextView) view.findViewById(R.id.title)).setText(document.name);
		((TextView) view.findViewById(R.id.description)).setText(document.filename);
		return view;
	}

	@Override public int getCount() {
		return documents.length;
	}

	@Override public Document getItem(int position) {
		return documents[position];
	}

	@Override public long getItemId(int position) {
		return position;
	}

	@Override public int getItemViewType(int position) {
		return 0;
	}

	@Override public int getViewTypeCount() {
		return 1;
	}

	@Override public boolean hasStableIds() {
		return false;
	}

	@Override public boolean isEmpty() {
		return false;
	}

	@Override public void registerDataSetObserver(DataSetObserver observer) {
	}

	@Override public void unregisterDataSetObserver(DataSetObserver observer) {
	}

	@Override public boolean areAllItemsEnabled() {
		return false;
	}

	@Override public boolean isEnabled(int position) {
		return true;
	}

}
