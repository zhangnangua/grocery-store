package com.pumpkin.applets_container.helper

import androidx.viewbinding.ViewBinding
import com.pumpkin.applets_container.databinding.LayoutSettingItemBinding
import com.pumpkin.applets_container.databinding.VhBigCardBinding
import com.pumpkin.applets_container.databinding.VhCommonHorizontalBinding
import com.pumpkin.applets_container.databinding.VhCommonListItemBinding
import com.pumpkin.applets_container.databinding.VhEditorPickItemBinding
import com.pumpkin.applets_container.databinding.VhMineNavigationLayoutBinding
import com.pumpkin.applets_container.databinding.VhOfflineBigCardStyle1Binding
import com.pumpkin.applets_container.databinding.VhOfflineCardStyle1Binding
import com.pumpkin.applets_container.databinding.VhRecentItemBinding
import com.pumpkin.applets_container.databinding.VhSearchItemBinding
import com.pumpkin.applets_container.databinding.VhTitleBinding
import com.pumpkin.applets_container.databinding.VhWordCardStyle1Binding
import com.pumpkin.applets_container.view.vh.BigCardVH
import com.pumpkin.applets_container.view.vh.CommonListItemVH
import com.pumpkin.applets_container.view.vh.EditorPickHorizontalVH
import com.pumpkin.applets_container.view.vh.EditorPickItemVH
import com.pumpkin.applets_container.view.vh.MineNavigationVH
import com.pumpkin.applets_container.view.vh.MineSettingItemVH
import com.pumpkin.applets_container.view.vh.OfflineBigCardStyle1VH
import com.pumpkin.applets_container.view.vh.OfflineCardStyle1VH
import com.pumpkin.applets_container.view.vh.RecentHorizontalVH
import com.pumpkin.applets_container.view.vh.RecentItemVH
import com.pumpkin.applets_container.view.vh.SearchItemVH
import com.pumpkin.applets_container.view.vh.TitleVH
import com.pumpkin.applets_container.view.vh.WordCardStyle1VH
import com.pumpkin.mvvm.adapter.BaseVH
import com.pumpkin.mvvm.adapter.TypeHelper

object VHRegister {

    fun register() {

//        TypeHelper.register(CarouselItemVH.TYPE) { layoutInflater, context, parent, requestManager ->
//            CarouselItemVH(VhCarouselItemBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
//        }

//        TypeHelper.register(CarouselHorizontalVH.TYPE) { layoutInflater, context, parent, requestManager ->
//            CarouselHorizontalVH(VhCommonHorizontalBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
//        }

        TypeHelper.register(BigCardVH.TYPE) { layoutInflater, context, parent, requestManager ->
            BigCardVH(VhBigCardBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(RecentHorizontalVH.TYPE) { layoutInflater, context, parent, requestManager ->
            RecentHorizontalVH(VhCommonHorizontalBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(RecentItemVH.TYPE) { layoutInflater, context, parent, requestManager ->
            RecentItemVH(VhRecentItemBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(EditorPickHorizontalVH.TYPE) { layoutInflater, context, parent, requestManager ->
            EditorPickHorizontalVH(VhCommonHorizontalBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(TitleVH.TYPE) { layoutInflater, context, parent, requestManager ->
            TitleVH(VhTitleBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(WordCardStyle1VH.TYPE) { layoutInflater, context, parent, requestManager ->
            WordCardStyle1VH(VhWordCardStyle1Binding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(OfflineCardStyle1VH.TYPE) { layoutInflater, context, parent, requestManager ->
            OfflineCardStyle1VH(VhOfflineCardStyle1Binding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(EditorPickItemVH.TYPE) { layoutInflater, context, parent, requestManager ->
            EditorPickItemVH(VhEditorPickItemBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(SearchItemVH.TYPE) { layoutInflater, context, parent, requestManager ->
            SearchItemVH(VhSearchItemBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(OfflineBigCardStyle1VH.TYPE) { layoutInflater, context, parent, requestManager ->
            OfflineBigCardStyle1VH(VhOfflineBigCardStyle1Binding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(MineSettingItemVH.TYPE) { layoutInflater, context, parent, requestManager ->
            MineSettingItemVH(LayoutSettingItemBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(MineNavigationVH.TYPE) { layoutInflater, context, parent, requestManager ->
            MineNavigationVH(VhMineNavigationLayoutBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(CommonListItemVH.TYPE) { layoutInflater, context, parent, requestManager ->
            CommonListItemVH(VhCommonListItemBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }
    }
}