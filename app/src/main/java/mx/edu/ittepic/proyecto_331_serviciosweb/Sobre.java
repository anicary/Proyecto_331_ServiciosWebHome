package mx.edu.ittepic.proyecto_331_serviciosweb;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

public class Sobre extends MaterialAboutActivity {
    @Override
    protected MaterialAboutList getMaterialAboutList(Context context) {
        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();
        appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text("Proyecto Unidad 3.3.1 Servicios Web")
                .desc("© 2018 Ana Carolina Mondragon Rangel")
                .icon(R.mipmap.ic_launcher)
                .build());
        appCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(context,
                new IconicsDrawable(context)
                        .icon(CommunityMaterial.Icon.cmd_information_outline)
                        .color(ContextCompat.getColor(context, R.color.mal_color_icon_light_theme))
                        .sizeDp(18),
                "Version",
                false));
        MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();
        authorCardBuilder.title("Author");
        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(" Ana Carolina Mondragon Rangel ")
                .subText("Tepic,Nayarit,Mexico")
                .icon(new IconicsDrawable(context)
                        .icon(CommunityMaterial.Icon.cmd_account)
                        .color(ContextCompat.getColor(context, R.color.mal_color_icon_light_theme))
                        .sizeDp(18))
                .build());
        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Sigueme en github")
                .icon(new IconicsDrawable(context)
                        .icon(CommunityMaterial.Icon.cmd_github_circle)
                        .color(ContextCompat.getColor(context, R.color.mal_color_icon_light_theme))
                        .sizeDp(18))
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(context, Uri.parse("https://github.com/anicary")))
                .build());
        return new MaterialAboutList(appCardBuilder.build(), authorCardBuilder.build());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_MaterialAboutActivity_Fragment);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.mal_title_about);
    }

}
