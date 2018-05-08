<#include "header.ftl" parse=true>
<link rel="stylesheet" href="../styles/letter.css">
    <div id="main">
        <div class="zg-wrap zu-main clearfix ">
            <ul class="letter-list">
                <#list conversations as conversation>
                <li id="conversation-item-10001_622873">
                <a class="letter-link" href="/msg/detail?conversationId=${conversation.message.conversationId}">
                </a>
                <div class="letter-info">
                    <span class="l-time">${conversation.message.createdDate?string("yyyy-MM-dd hh:mm:ss")!}</span>
                <div class="l-operate-bar">
                    <!--
                <a href="javascript:void(0);" class="sns-action-del" data-id="10001_622873">
                删除
                </a>
                -->
                <a href="/msg/detail?conversationId=${conversation.message.conversationId}">
                    ${conversation.message.id} <!--id存了未读数-->
                </a>
                </div>
                </div>
                <div class="chat-headbox">
                <span class="msg-num">
                ${conversation.unreadCount!}
                </span>
                <a class="list-head">
                <img alt="头像" src="${conversation.user.headUrl}">
                </a>
                </div>
                <div class="letter-detail">
                <a title="${conversation.user.name}" class="letter-name level-color-1">
                    ${conversation.user.name}
                </a>
                <p class="letter-brief">
                <a href="/msg/detail?conversationId=${conversation.message.conversationId}">
                ${conversation.message.content}
                </a>
                </p>
                </div>
                </li>
                </#list>
            </ul>
        </div>
    </div>
<#include "footer.ftl" parse=true>