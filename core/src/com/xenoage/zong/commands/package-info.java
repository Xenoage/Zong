/**
 * The commands in Zong! are based on the Xenoage Utils Document Framework.
 * 
 * When a command is executed, it may execute additional commands internally.
 * If undoable, the command is responsible for undoing the internally executed
 * commands in reverse order.
 * 
 * Commands may provide an additional boolean parameter <code>undoable</code>, that can be used
 * to run the command much faster, but it provides no undo functionality in this case. This is useful for commands
 * which have a large computational overhead if undo support is needed.
 * 
 * There are some rules which each command must obey:
 * <ol>
 *   <li>Reference Integrity: When a command is undone, it must perfectly restore
 *     the old state of the score. Not only equals-by-value (<code>oldData.equals(newData)</code>)
 *     but referential equality (<code>oldData == newData</code>) is required. This is because backup
 *     data for undo may contain references for performance reasons.</li>
 *   <li>When a static execute-method is called directly, the integrity of the previous undo stack can no longer
 *     be guaranteed, so undoing the previous operations is not possible any more. The caller of the
 *     execute-method is responsible to clear the corresponding undo stack.</li>
 *   <li>The naming is as follows (where appropriate): [Object][Verb] (like ChordWrite).</li>
 * </ol>
 * 
 * For more details about these design decisions, read the "2013-05-02 Mutable Core Concept".
 * 
 * @author Andreas Wenger
 */
package com.xenoage.zong.commands;