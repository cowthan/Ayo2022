package org.ayo.richeditor.demo;

import org.ayo.core.Lang;
import org.ayo.editor.emoj.db.EmojiRepo;
import org.ayo.editor.emoj.model.EmojiCategoryModel;
import org.ayo.editor.emoj.model.EmojiModel;
import org.ayo.file.IO;
import org.ayo.http.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/21.
 */

public class MyEmojiRepo implements EmojiRepo {

    private List<EmojiPackagesModel> packages;

    public MyEmojiRepo(){
        packages = JsonUtils.getBeanList(getPackageJsons(), EmojiPackagesModel.class);
    }

    private String getPackageJsons(){
        String s = getFileContent("emoj/pkg.json");
        return s;
    }

    private String getEmojiDir(int packageId){
        return "emoj/emoj/" + packageId;
    }

    private String getFileContent(String path){
        return IO.string(IO.fromAssets(path));
    }

    private String getIconAbsoluteUri(int pkgId, String filename){
        return "file:///android_asset/" + "emoj/emoj/" + pkgId + "/" + filename;
    }


    @Override
    public List<EmojiModel> queryAllEmojiList() {
        List<EmojiModel> list = new ArrayList<>();

        for(EmojiPackagesModel pkg: packages){
            list = (List<EmojiModel>) Lang.combine(list, queryEmojiListByPackageId(pkg.id));
        }

        return list;
    }

    @Override
    public List<EmojiModel> queryEmojiListByPackageId(int pkgId) {
        String dirPath = getEmojiDir(pkgId);
        String jsonFilePath = dirPath + "/info.json";
        String json = getFileContent(jsonFilePath);

        List<EmojiModel> list = JsonUtils.getBeanList(json, EmojiModel.class);
        for(EmojiModel m: list){
            /*
             "filename": "1",
             "name": "[kanbuxiaoqu]",
             "alias": "[看不下去]"
             */
            m.pkgId = pkgId;
            m.type = EmojiModel.TYPE_SMALL;
            m.icon = getIconAbsoluteUri(pkgId, m.filename);
        }
        return list;
    }

    @Override
    public List<EmojiCategoryModel> queryNormalEmojiList() {
        List<EmojiCategoryModel> list = new ArrayList<>();
        for(EmojiPackagesModel packagesModel: packages){
            EmojiCategoryModel model = new EmojiCategoryModel();
            model.setPkgId(packagesModel.id);
            model.setData( queryEmojiListByPackageId(packagesModel.id));
            model.setScenario(packagesModel.scenario);
            model.setType(packagesModel.type);
            model.setName(packagesModel.name);
            model.setIcon(packagesModel.icon);
            list.add(model);
        }
        return list;
    }

}
